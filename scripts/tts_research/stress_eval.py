"""Forced alignment (torchaudio MMS_FA) plus per-syllable length, loudness and pitch, scored
against clips the owner labelled by ear. Result on 2026-10-02: 34/46 absolute, 16/19 pairwise.
Not reliable enough to gate clips. The answer key below is the ear labels from that day.

Usage: stress_eval.py <folder holding the audition clips>
"""
import subprocess,tempfile,os,sys,json,itertools
import numpy as np,torch,torchaudio,parselmouth
from pathlib import Path
torch.set_num_threads(6)
R=Path(sys.argv[1])
UK=dict(zip("абвгґдежзиіїйклмнопрстуфхцчшщьюяє","a b v h g d e zh z y i i i k l m n o p r s t u f kh ts ch sh sh  u a e".split(" ")))
def rom(w): return "".join(UK.get(ch,ch) for ch in w.lower() if ch.isalpha())
K=[] # (file, romanized word, vowel set, truth syllable, group)
def add(f,w,t,g,v="aeiou"): K.append((f,w,v,t,g))
add("ipa-tier1/de-DE-umfahren-insep-chirp.mp3","umfahren",2,"de umfahren chirp")
add("ipa-tier1/de-DE-umfahren-sep-chirp.mp3","umfahren",1,"de umfahren chirp")
add("ipa-tier1/en-US-record-noun-chirp.mp3","record",1,"en record chirp")
add("ipa-tier1/en-US-record-verb-chirp.mp3","record",2,"en record chirp")
add("ipa-tier1/en-US-record-noun-gemini.mp3","record",2,"en record gemini")
add("ipa-tier1/en-US-record-verb-gemini.mp3","record",2,"en record gemini")
for e in ("chirp","gemini"):
    add(f"ipa-tier1/es-ES-publico-noun-{e}.mp3","publico",1,f"es publico {e}")
    add(f"ipa-tier1/es-ES-publico-verb-{e}.mp3","publico",3,f"es publico {e}")
add("ipa-tier1/uk-UA-zamok-lock-gemini.mp3","zamok",1,"uk zamok gemini","aouiye")
add("ipa-tier1/uk-UA-zamok-castle-gemini.mp3","zamok",1,"uk zamok gemini","aouiye")
for w in ("principi","subito"):
    add(f"italian/{w}-1.mp3",w,1,f"it {w} chirp"); add(f"italian/{w}-2.mp3",w,2,f"it {w} chirp")
wn=[("замок",1),("замок",2),("мука",1),("мука",2),("атлас",1),("атлас",2),("орган",1),("орган",2),("плачу",1),("плачу",2),("руки",1),("руки",2),("брати",1),("брати",2),("дорога",2),("дорога",3)]
for i,(w,t) in enumerate(wn): add(f"uk-wavenet/s0-{i}.mp3",rom(w),t,f"uk {w} wavenet","aouiye")
for f,w,t in [("замок-2-apos","замок",2),("замок-1-apos","замок",2),("мука-2-apos","мука",1),("мука-2-hyph","мука",1),("руки-1-apos","руки",1),("руки-2-apos","руки",2),("руки-2-hyph","руки",2),("плачу-2-hyph","плачу",2),("плачу-1-apos","плачу",1),("плачу-2-apos","плачу",1),("плачу-1-hyph","плачу",1)]:
    add(f"uk-apostrophe/{f}.mp3",rom(w),t,f"uk {w} chirp-trick","aouiye")
for f in ("c5-apos","c7-hyphen","c8-latin"): add(f"uk-male/{f}.mp3","zamok",2,"uk замок chirp-trick","aouiye")
bundle=torchaudio.pipelines.MMS_FA; model=bundle.get_model(with_star=False); tok=bundle.get_tokenizer(); aligner=bundle.get_aligner()
def load(p):
    t=tempfile.NamedTemporaryFile(suffix=".wav",delete=False).name
    subprocess.run(["ffmpeg","-v","error","-y","-i",str(p),"-ac","1","-ar","16000",t],check=True); return t
def analyse(f,word,vowels):
    wav=load(R/f); wf,sr=torchaudio.load(wav)
    with torch.inference_mode(): em,_=model(wf)
    spans=aligner(em[0],tok([word]))[0]; ratio=wf.size(1)/em.size(1)/sr
    snd=parselmouth.Sound(wav); os.unlink(wav)
    it=snd.to_intensity(time_step=0.005); pt=snd.to_pitch(time_step=0.005)
    # segment i runs from its own start to the next letter's start; the last one to the end of voiced activity
    st=[sp.start*ratio for sp in spans]
    Iall=it.values[0]; xsall=it.xs(); act=xsall[Iall>Iall.max()-30]; end_t=float(act[-1]) if len(act) else spans[-1].end*ratio
    seg=[(st[i], st[i+1] if i+1<len(st) else max(end_t,spans[-1].end*ratio)) for i in range(len(st))]
    nuc=[]; cur=None
    for ch,(a,b) in zip(word,seg):
        if ch in vowels:
            if cur is None: cur=[a,b]
            else: cur[1]=b
        else:
            if cur: nuc.append(cur); cur=None
    if cur: nuc.append(cur)
    feats=[]
    for a,b in nuc:
        xs=[x for x in it.xs() if a<=x<=b] or [ (a+b)/2 ]
        I=np.array([it.get_value(x) or 0 for x in xs]); I=np.nan_to_num(I)
        f0=np.array([pt.get_value_at_time(x) or np.nan for x in xs])
        feats.append(dict(dur=b-a,imean=float(I.mean()),imax=float(I.max()),energy=float((10**(I/10)).sum()*0.005),f0=float(np.nanmean(f0)) if np.isfinite(f0).any() else np.nan))
    return feats
def z(v):
    v=np.array(v,float); s=np.nanstd(v); return (v-np.nanmean(v))/(s if s>0 else 1)
methods={"duration":lambda F:z([f["dur"] for f in F]),"loudness peak":lambda F:z([f["imax"] for f in F]),"energy":lambda F:z([np.log(f["energy"]+1e-9) for f in F]),
 "pitch":lambda F:np.nan_to_num(z([f["f0"] for f in F]),nan=-9),"dur+loud":lambda F:z([f["dur"] for f in F])+z([f["imax"] for f in F]),"dur+loud+pitch":lambda F:z([f["dur"] for f in F])+z([f["imax"] for f in F])+np.nan_to_num(z([f["f0"] for f in F]))}
res=[]; 
for f,w,v,t,g in K:
    try: F=analyse(f,w,v)
    except Exception as e: print("FAIL",f,str(e)[:80]); continue
    res.append(dict(file=f,word=w,truth=t,group=g,F=F))
    print(f"{f:52} truth={t} nuclei={len(F)} "+" ".join(f"[{x['dur']*1000:.0f}ms {x['imax']:.0f}dB {x['f0']:.0f}Hz]" for x in F),flush=True)
print("\n== ABSOLUTE: pick the most prominent syllable in one clip")
for name,m in methods.items():
    ok=sum(int(np.argmax(m(r["F"]))+1==r["truth"]) for r in res); print(f"{name:16} {ok}/{len(res)}")
print("\n== RELATIVE: within a pair of same word and engine with different truth, does the share of syllable k rise in the clip stressed on k")
pairs=[]
for g,rs in itertools.groupby(sorted(res,key=lambda r:r["group"]),key=lambda r:r["group"]):
    rs=list(rs)
    for a,b in itertools.combinations(rs,2):
        if a["truth"]!=b["truth"] and len(a["F"])==len(b["F"]): pairs.append((a,b))
for name,m in methods.items():
    ok=0
    for a,b in pairs:
        ma,mb=m(a["F"]),m(b["F"]); ka,kb=a["truth"]-1,b["truth"]-1
        # contrast between the two candidate syllables should flip sign between the clips
        ok+=int((ma[ka]-ma[kb])>(mb[ka]-mb[kb]))
    print(f"{name:16} {ok}/{len(pairs)}")
json.dump(res,open(R/"stress_eval.json","w"),ensure_ascii=False)

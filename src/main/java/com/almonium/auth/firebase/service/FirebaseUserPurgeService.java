package com.almonium.auth.firebase.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Empties the Firebase user directory. Firebase is the third store behind an account - Postgres and
 * Stream being the others - and the only one a database drop cannot reach, so a reset that skips it
 * leaves accounts that can no longer sign in: the row they were provisioned into is gone, and the
 * create path then demands a verified email the old account never had.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FirebaseUserPurgeService {
    FirebaseAuthGateway firebaseAuthGateway;

    /** The project id, which is what an operator has to type: it names what actually gets emptied. */
    public String confirmationPhrase() {
        return firebaseAuthGateway.projectId();
    }

    public int countUsers() {
        return firebaseAuthGateway.listAllUserIds().size();
    }

    /**
     * Deletes every Firebase account, optionally sparing the operator's own. Sparing it is the
     * default because the console this is triggered from needs an admin to sign in, and adminhood is
     * a Firebase custom claim: delete yourself and you have to register again and re-run the claim
     * script before you can reach this page a second time.
     */
    public int purge(String operatorUid, boolean includeOperator) {
        List<String> uids = firebaseAuthGateway.listAllUserIds().stream()
                .filter(uid -> includeOperator || !uid.equals(operatorUid))
                .toList();

        if (uids.isEmpty()) {
            return 0;
        }

        firebaseAuthGateway.deleteUsers(uids);
        log.warn("Purged {} Firebase accounts (operator {})", uids.size(), includeOperator ? "included" : "spared");
        return uids.size();
    }
}

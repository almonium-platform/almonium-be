import matplotlib.pyplot as plt
import numpy as np
from matplotlib.patches import FancyBboxPatch
from matplotlib.transforms import Affine2D
from matplotlib.colors import LinearSegmentedColormap, Normalize

EDGECOLOR = 'white'

# Parameters
num_rectangles = 10  # Number of rectangles, matching 360 degrees / 10 degrees
angle_shift = 360 / num_rectangles  # Angle between each rectangle
radius = 6  # Radius of the inner circle (tangent to the bottom of rectangles)
rect_width = 10
rect_height = 6
corner_radius = 1.5  # Radius of the rounded corners

# Offsets for moving the drawing
x_offset = 0  # Move X units to the right
y_offset = 0  # Move Y units up

# Create a gradient colormap
cmap = LinearSegmentedColormap.from_list('my_cmap', ['#d62f2f', '#251191'])  # From red to blue
norm = Normalize(vmin=0, vmax=num_rectangles)

# Create figure and axis
fig, ax = plt.subplots(figsize=(10, 10), dpi=300)

# Variables to track the bounding box
x_min, x_max = np.inf, -np.inf

for i in range(num_rectangles):
    angle = np.deg2rad(i * angle_shift)

    # Calculate the position where the bottom middle of the rectangle should sit on the circle
    x = radius * np.cos(angle) + x_offset
    y = radius * np.sin(angle) + y_offset

    # Correct alignment: shift the rectangle so that its bottom edge is tangent to the circle
    x_rect_offset = rect_height / 2 * np.sin(angle) - 0
    y_rect_offset = rect_height / 2 * np.cos(angle) + 1

    # Determine the color from the gradient
    color = cmap(norm(i))
    # color = 'black'

    # Draw the rounded rectangle using FancyBboxPatch
    rect = FancyBboxPatch(
        (x - rect_width / 2 + x_rect_offset, y - rect_height / 2 + y_rect_offset),
        rect_width, rect_height,
        boxstyle=f"round,pad=0.05,rounding_size={corner_radius}",
        edgecolor=EDGECOLOR, facecolor=color, linewidth=5
    )

    # Apply rotation transformation around the center of the rectangle
    transform = Affine2D().rotate_around(x, y, angle) + ax.transData
    rect.set_transform(transform)

    ax.add_patch(rect)

    # Update bounding box
    rect_corners = np.array([
        [x - rect_width / 2, y - rect_height / 2],
        [x + rect_width / 2, y + rect_height / 2],
    ])
    x_min = min(x_min, rect_corners[:, 0].min())
    x_max = max(x_max, rect_corners[:, 0].max())

ax.set_aspect('equal')

# Find the horizontal center
center_x = (x_min + x_max) / 2

# Adjust limits to show only the left half of the figure and ensure no part is cut off
plt.xlim( -25 + x_offset, 25 + x_offset)  # Expand leftmost part slightly
# plt.xlim(x_min - 1, center_x)  # Expand leftmost part slightly
plt.ylim(-25 + y_offset, 25 + y_offset)
plt.axis('off')

plt.show()

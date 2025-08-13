
# CS 330 OpenGL Project – 3D Still Life Scene
# "Developer: Daniel Gorelkin"

## 📌 Overview
This project is a 3D-rendered **still life scene** built using **OpenGL** as part of the CS 330 final project.  
The scene consists of a **stacked composition** of objects — a base plane, backdrop wall, pitcher, book, and pencil — rendered with realistic object placement, lighting, and shading.  
All objects are constructed from basic 3D shapes such as boxes, cones, cylinders, spheres, and tori.

## 🖼 The Sketch
| ![Sketch 1](screenshots/screenshot1.png) | ![Screenshot 2](screenshots/screenshot2.png) |

---

## 🎥 Demo Video
[![Watch the Demo](screenshots/demo_thumbnail.png)](https://youtu.be/MjIdYK4Emp8)

---

## 🖼 Screenshots
| Scene View | Alternate Angle |
|------------|-----------------|
| ![Screenshot 1](screenshots/screenshot1.png) | ![Screenshot 2](screenshots/screenshot2.png) | ![Screenshot 3](screenshots/screenshot3.png) |

---

## 🚀 Features
- **Interactive Navigation**
  - **WASD**: Move camera forward, left, backward, right
  - **Q/E**: Move camera up/down along Y-axis
  - **O/P**: Switch between orthographic & perspective projection
  - **Mouse Look**: Rotate camera view
  - **Mouse Scroll / Ctrl + Scroll**: Adjust movement speed & sensitivity

- **3D Object Construction**
  - Pitcher: Torus + Cylinder + Half Sphere
  - Book: Box shape
  - Pencil: Cylinder + Cone
  - Base plane & backdrop: Box shapes

- **Lighting & Texturing**
  - Custom shaders for color, texture, UV scaling, and materials
  - Configurable scene lighting

- **Modular & Reusable Code**
  - `ShapeMeshes.cpp` for defining base meshes
  - `SceneManager.cpp` for textures, materials, and lights
  - `ViewManager.cpp` for navigation logic
  - Easily replaceable components for future upgrades (e.g., VR, joystick support)

---

## 🛠 Built With
- **C++**
- **OpenGL**
- **GLFW**, **GLM**, **GLEW**, **GLAD**
- **Custom GLSL Shaders**

---

## 📂 File Structure

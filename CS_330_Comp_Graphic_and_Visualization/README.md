
# CS 330 OpenGL Project – 3D Still Life Scene
# Developer: Daniel Gorelkin

## 📢 Insights
While working on this project, I explored and studied the functionality of OpenGL libraries and their implementations in modern software solutions. Specifically, I developed a set of skills to render a 3D scene compiled from a set of vertex meshes onto a 2D screen. Additionally, while using the given libraries, I created a dynamic space that can be virtually navigated following a set of input commands. More specifically, I practice creating various 3D objects and setting them up in a defined world, applying light effects and textures to the objects as well as creating navigation in the world by using matrices and rendering loops. The explored libraries and studied techniques can be applied across various fields and industries that require projecting a 3D scene onto 2D displays such as screens, VR goggles, hologram projectors, and more.
To develop this project, I employed various strategies to establish the initial space, including defining all objects in the scene through scaling, rotating, illuminating, and combining complex objects with simple ones to construct the scene. The iteration factor in this project was implemented as the navigation functionality that allowed the effect of moving the view camera in the created world. The rapid graphical computation enabled the navigation and transformation of objects on the X, Y, and Z axes within the created world. The steps taken to complete this project taught me to work in phases, e.g., setting up the world, adding objects into the world, shading and adding textures to the objects, and lastly, adding the dynamic functionality for the navigation within the scene. That approach was implemented with great focus on reusability and modularity, which allows the technique to be transformed to larger-scale and more complex projects.
With that said, the experience working with computational graphics and visualizations gives me new knowledge and skills that can be applied in various industries, from game development, the health care sector, imaging, and many other digital sectors that rely on image rendering and graphic visualization.

## 📌 Overview
This project is a 3D-rendered **still life scene** built using **OpenGL** as part of the CS 330 final project.  
The scene consists of a **stacked composition** of objects — a base plane, backdrop wall, pitcher, book, and pencil — rendered with realistic object placement, lighting, and shading.  
All objects are constructed from basic 3D shapes such as boxes, cones, cylinders, spheres, and tori.

## 🖼 The Sketch
| ![Sketch 1](https://github.com/Dgoralkin/Portfolio/blob/main/CS_330_Comp_Graphic_and_Visualization/Screenshots/Picture1.png) | ![Screenshot 2](https://github.com/Dgoralkin/Portfolio/blob/main/CS_330_Comp_Graphic_and_Visualization/Screenshots/Picture2.jpg) |

---

## 🎥 Demo Video
[![Watch the Demo](https://github.com/Dgoralkin/Portfolio/blob/main/CS_330_Comp_Graphic_and_Visualization/Screenshots/Demo.gif)](https://youtu.be/MjIdYK4Emp8)

---

## 🖼 Screenshots
| Scene View | Perspective View | Orthographic View |
|------------|-----------------|-----------------|
| ![Screenshot 1](https://github.com/Dgoralkin/Portfolio/blob/main/CS_330_Comp_Graphic_and_Visualization/Screenshots/Screenshot_1.png) | ![Screenshot 2](https://github.com/Dgoralkin/Portfolio/blob/main/CS_330_Comp_Graphic_and_Visualization/Screenshots/Screenshot_3.png) | ![Screenshot 3](https://github.com/Dgoralkin/Portfolio/blob/main/CS_330_Comp_Graphic_and_Visualization/Screenshots/Screenshot_2.png) |

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
📦 Project
┣ 📂 src
┃ ┣ ShapeMeshes.cpp
┃ ┣ SceneManager.cpp
┃ ┣ ViewManager.cpp
┃ ┣ ShaderManager.cpp
┃ ┗ main.cpp
┣ 📂 shaders
┣ 📂 textures
┣ 📂 screenshots
┗ README.md
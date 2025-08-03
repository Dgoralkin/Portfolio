///////////////////////////////////////////////////////////////////////////////
// viewmanager.h
// ============
// manage the viewing of 3D objects within the viewport
//
//  AUTHOR: Brian Battersby - SNHU Instructor / Computer Science
//	Created for CS-330-Computational Graphics and Visualization, Nov. 1st, 2023
///////////////////////////////////////////////////////////////////////////////

#include "ViewManager.h"

// GLM Math Header inclusions
#include <glm/glm.hpp>
#include <glm/gtx/transform.hpp>
#include <glm/gtc/type_ptr.hpp>    

// declarations for the global variables and defines
namespace
{
	// Variables for window width and height
	const int WINDOW_WIDTH = 1000;
	const int WINDOW_HEIGHT = 800;
	const char* g_ViewName = "view";
	const char* g_ProjectionName = "projection";

	// camera object used for viewing and interacting with
	// the 3D scene
	Camera* g_pCamera = nullptr;

	// these variables are used for mouse movement processing
	float gLastX = WINDOW_WIDTH / 2.0f;
	float gLastY = WINDOW_HEIGHT / 2.0f;
	bool gFirstMouse = true;

	// time between current frame and last frame
	float gDeltaTime = 0.0f; 
	float gLastFrame = 0.0f;

	// the bOrthographicProjection variable switches between orthographic, and perspective projection.
	// when false, the camera is set to perspective projection by default.
	// when true, the camera is set to orthographic projection by default.
	bool bOrthographicProjection = false;

	// set default camera for the perspective view (position, front, up, and zoom vectors)
	// ***********************************************************************************
	// set the camera position vector
	glm::vec3 gDefaultCameraPosition = glm::vec3(0.0f, 5.5f, 8.0f);
	// set the camera front (look at) vector
	glm::vec3 gDefaultCameraFront = glm::vec3(0.0f, -0.5f, -2.0f);
	// set the camera up vector
	glm::vec3 gDefaultCameraUp = glm::vec3(0.0f, 1.0f, 0.0f);
	// set the default camera zoom level
	const float gDefaultCameraZoom = 90.0f;
}

/***********************************************************
 *  ViewManager()
 *
 *  The constructor for the class
 ***********************************************************/
ViewManager::ViewManager(
	ShaderManager *pShaderManager)
{
	// initialize the member variables
	m_pShaderManager = pShaderManager;
	m_pWindow = NULL;

	// set the camera position, front and up vectors
	// Create the camera object
	g_pCamera = new Camera();
	// set the camera position vector
	g_pCamera->Position = gDefaultCameraPosition;
	// set the camera direction vector
	g_pCamera->Front = gDefaultCameraFront;
	// set the camera up vector	vector
	g_pCamera->Up = gDefaultCameraUp;
	// set the camera zoom vector
	g_pCamera->Zoom = gDefaultCameraZoom;
}

/***********************************************************
 *  ~ViewManager()
 *
 *  The destructor for the class
 ***********************************************************/
ViewManager::~ViewManager()
{
	// free up allocated memory
	m_pShaderManager = NULL;
	m_pWindow = NULL;
	if (NULL != g_pCamera)
	{
		delete g_pCamera;
		g_pCamera = NULL;
	}
}

/***********************************************************
 *  Mouse_Scroll_Callback()
 *
 *  This method is automatically called from GLFW whenever
 *  the mouse scroll wheel is moved within the active GLFW display window.
 *	This method is used to adjust the camera movement speed when the mouse wheel is scrolled -
 *	and to adjust the camera mouse sensitivity when Ctrl is pressed and the mouse wheel is scrolled.
 ***********************************************************/
void Mouse_Scroll_Callback(GLFWwindow* window, double xoffset, double yoffset)
{
	// If the camera object is null, then exit this method
	if (NULL == g_pCamera || window == NULL)
	{
		std::cout << "Camera or Window is null:"
			<< " Camera = " << g_pCamera
			<< ", Window = " << window << std::endl;
		return;
	}

	// Adjust the camera/mouse sensitivity case
	// Check if left or right Ctrl button is pressed
	if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS ||
		glfwGetKey(window, GLFW_KEY_RIGHT_CONTROL) == GLFW_PRESS)
	{
		// std::cout << "Ctrl + Mouse Wheel detected! yoffset: " << yoffset << std::endl;

		// Adjust the camera mouse sensitivity based on the scroll wheel movement increment of yoffset by 0.03f units
		g_pCamera->MouseSensitivity += static_cast<float>(yoffset / 100 * 2);

		// Ensure the sensitivity is within a reasonable range of 0.01f to 0.25f
		if (g_pCamera->MouseSensitivity < 0.01f)
		{
			g_pCamera->MouseSensitivity = 0.01f; // Minimum sensitivity
		}
		else if (g_pCamera->MouseSensitivity > 0.25f)
		{
			g_pCamera->MouseSensitivity = 0.25f; // Maximum sensitivity
		}
		// Print the current camera sensitivity to the console for debugging
		// std::cout << "Camera Mouse Movement Sensitivity: " << g_pCamera->MouseSensitivity << std::endl;
	}

	// Adjust the camera movement speed case (Normal scroll behavior)
	else
	{
		// Adjust the camera movement speed based on the scroll wheel movement increment of yoffset by 1.0f units
		g_pCamera->ProcessMouseScroll(static_cast<float>(-yoffset));
		// Print the current camera speed to the console for debugging
		// std::cout << "Camera Movement Speed: " << g_pCamera->MovementSpeed << std::endl;
	}
}

/***********************************************************
 *  CreateDisplayWindow()
 *
 *  This method is used to create the main display window.
 ***********************************************************/
GLFWwindow* ViewManager::CreateDisplayWindow(const char* windowTitle)
{
	GLFWwindow* window = nullptr;

	// try to create the displayed OpenGL window
	window = glfwCreateWindow(
		WINDOW_WIDTH,
		WINDOW_HEIGHT,
		windowTitle,
		NULL, NULL);
	if (window == NULL)
	{
		std::cout << "Failed to create GLFW window" << std::endl;
		glfwTerminate();
		return NULL;
	}
	glfwMakeContextCurrent(window);

	// this callback is used to receive mouse moving events
	glfwSetCursorPosCallback(window, &ViewManager::Mouse_Position_Callback);

	// this callback is used to receive mouse scroll events and adjust the camera movement speed and mouse/camera sensitivity
	glfwSetScrollCallback(window, Mouse_Scroll_Callback);

	// tell GLFW to capture all mouse events and hide the mouse cursor
	// disabling the mouse cursor allows for free movement of the camera 360° on the X and Y axes.
	// Camewra angle limiting could be adjusted theough the camera class in the ProcessMouseMovement() method.
	glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

	// enable blending for supporting tranparent rendering
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	m_pWindow = window;

	return(window);
}

/***********************************************************
 *  Mouse_Position_Callback()
 *
 *  This method is automatically called from GLFW whenever
 *  the mouse is moved within the active GLFW display window.
 ***********************************************************/
void ViewManager::Mouse_Position_Callback(GLFWwindow* window, double xMousePos, double yMousePos)
{
	// when the first mouse move event is received, this needs to be recorded so that
	// all subsequent mouse moves can correctly calculate the X position offset and Y
	// position offset for proper operation
	if (gFirstMouse)
	{
		gLastX = xMousePos;
		gLastY = yMousePos;
		gFirstMouse = false;
	}

	// calculate the X offset and Y offset values for moving the 3D camera accordingly
	float xOffset = xMousePos - gLastX;
	float yOffset = gLastY - yMousePos; // reversed since y-coordinates go from bottom to top

	// set the current positions into the last position variables
	gLastX = xMousePos;
	gLastY = yMousePos;

	// move the 3D camera according to the calculated offsets
	g_pCamera->ProcessMouseMovement(xOffset, yOffset);
}

/***********************************************************
 *  ProcessKeyboardEvents()
 *
 *  this method is called to process any keyboard events and control the camera
 *	this method allows the user to interact with the 3D scene
 *	by moving the camera around the scene and changing the camera view
 *	between perspective and orthographic projection.
 ***********************************************************/
void ViewManager::ProcessKeyboardEvents()
{
	// close the window if the escape key has been pressed
	if (glfwGetKey(m_pWindow, GLFW_KEY_ESCAPE) == GLFW_PRESS)
	{
		glfwSetWindowShouldClose(m_pWindow, true);
	}

	// if the camera object is null, then exit this method
	if (NULL == g_pCamera)
	{
		return;
	}

	// process camera zooming in and out
	if (glfwGetKey(m_pWindow, GLFW_KEY_W) == GLFW_PRESS)
	{
		g_pCamera->ProcessKeyboard(FORWARD, gDeltaTime);
	}
	if (glfwGetKey(m_pWindow, GLFW_KEY_S) == GLFW_PRESS)
	{
		g_pCamera->ProcessKeyboard(BACKWARD, gDeltaTime);
	}

	// process camera panning left and right
	if (glfwGetKey(m_pWindow, GLFW_KEY_A) == GLFW_PRESS)
	{
		g_pCamera->ProcessKeyboard(LEFT, gDeltaTime);
	}
	if (glfwGetKey(m_pWindow, GLFW_KEY_D) == GLFW_PRESS)
	{
		g_pCamera->ProcessKeyboard(RIGHT, gDeltaTime);
	}

	// process camera panning up and down
	if (glfwGetKey(m_pWindow, GLFW_KEY_Q) == GLFW_PRESS)
	{
		g_pCamera->ProcessKeyboard(UP, gDeltaTime);
	}
	if (glfwGetKey(m_pWindow, GLFW_KEY_E) == GLFW_PRESS)
	{
		g_pCamera->ProcessKeyboard(DOWN, gDeltaTime);
	}

	/***********************************************************
	*  change between perspective and orthographic projection views
	*  This functionality process user keyboard inputs
	*  to change the camera view from perspective to orthographic
	***********************************************************/
	// choose the front orthographic projection view
	if (glfwGetKey(m_pWindow, GLFW_KEY_O) == GLFW_PRESS)
	{
		// set the camera vectors to show the orthographic view
		// *****************************************************
		// change to orthographic projection
		bOrthographicProjection = true;
		// set the camera position vector
		g_pCamera->Position = glm::vec3(1.0f, 4.45f, 10.0f);
		// set the camera up vector
		g_pCamera->Up = glm::vec3(0.0f, 1.0f, 0.0f);
		// set the camera direction vector	vector
		g_pCamera->Front = glm::vec3(0.0f, 0.0f, -1.0f);
	}
	// choose the perspective projection view (default)
	if (glfwGetKey(m_pWindow, GLFW_KEY_P) == GLFW_PRESS)
	{
		// set the camera vectors to show the perspective view
		// ***************************************************
		// change to perspective projection
		bOrthographicProjection = false;
		// set the camera position vector
		g_pCamera->Position = gDefaultCameraPosition;
		// set the camera direction vector
		g_pCamera->Front = gDefaultCameraFront;
		// set the camera up vector	vector
		g_pCamera->Up = gDefaultCameraUp;
		// set the camera zoom vector
		g_pCamera->Zoom = gDefaultCameraZoom;
	}
}

/***********************************************************
 *  PrepareSceneView()
 *
 *  This method is used for preparing the 3D scene by loading
 *  the shapes, textures in memory to support the 3D scene rendering
 ***********************************************************/
void ViewManager::PrepareSceneView()
{
	glm::mat4 view;
	glm::mat4 projection;

	// per-frame timing
	float currentFrame = glfwGetTime();
	gDeltaTime = currentFrame - gLastFrame;
	gLastFrame = currentFrame;

	// process any keyboard events that may be waiting in the event queue and listen for user input
	ProcessKeyboardEvents();

	// get the current view matrix from the camera
	view = g_pCamera->GetViewMatrix();

	// define the prespective projection matrix
	if (bOrthographicProjection == false)
	{
		// perspective projection
		projection = glm::perspective(glm::radians(g_pCamera->Zoom), (GLfloat)WINDOW_WIDTH / (GLfloat)WINDOW_HEIGHT, 0.1f, 100.0f);
	}

	// define the orthographic projection matrix
	else
	{
		// front-view orthographic projection with scaled aspect ratio to fit the window size
		double scale = 0.0;
		// fit window for landscape display mode
		if (WINDOW_WIDTH > WINDOW_HEIGHT)
		{
			scale = (double)WINDOW_HEIGHT / (double)WINDOW_WIDTH;
			// define the scaled view frustum for orthographic landscape projection
			projection = glm::ortho(-5.0f, 5.0f, -5.0f*(float)scale, 5.0f*(float)scale, 0.1f, 100.0f);
		}
		// fit window for portrait display mode
		else if (WINDOW_WIDTH < WINDOW_HEIGHT)
		{
			scale = (double)WINDOW_WIDTH / (double)WINDOW_HEIGHT;
			// define the scaled view frustum for orthographic portrait projection
			projection = glm::ortho(-5.0f * (float)scale, 5.0f * (float)scale, -5.0f, 5.0f, 0.1f, 100.0f);
		}
		else
		{
			// define the view frustum for squared landscape projection without scaling
			projection = glm::ortho(-5.0f, 5.0f, -5.0f, 5.0f, 0.1f, 100.0f);
		}
	}

	// if the shader manager object is valid
	if (NULL != m_pShaderManager)
	{
		// set the view matrix into the shader for proper rendering
		m_pShaderManager->setMat4Value(g_ViewName, view);
		// set the view matrix into the shader for proper rendering
		m_pShaderManager->setMat4Value(g_ProjectionName, projection);
		// set the view position of the camera into the shader for proper rendering
		m_pShaderManager->setVec3Value("viewPosition", g_pCamera->Position);
	}
}
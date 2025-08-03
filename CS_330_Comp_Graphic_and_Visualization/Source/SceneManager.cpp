///////////////////////////////////////////////////////////////////////////////
// shadermanager.cpp
// ============
// manage the loading and rendering of 3D scenes
//
//  AUTHOR: Brian Battersby - SNHU Instructor / Computer Science
//	Created for CS-330-Computational Graphics and Visualization, Nov. 1st, 2023
///////////////////////////////////////////////////////////////////////////////

#include "SceneManager.h"

#ifndef STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_IMPLEMENTATION
#include "stb_image.h"
#endif

#include <glm/gtx/transform.hpp>

// declaration of global variables and defines
namespace
{
	const char* g_ModelName = "model";
	const char* g_ColorValueName = "objectColor";
	const char* g_TextureValueName = "objectTexture";
	const char* g_UseTextureName = "bUseTexture";
	const char* g_UseLightingName = "bUseLighting";
}


/***********************************************************
 *  SceneManager()
 *  The constructor for the class
 ***********************************************************/
SceneManager::SceneManager(ShaderManager *pShaderManager)
{
	m_pShaderManager = pShaderManager;
	// create the shape meshes object
	m_basicMeshes = new ShapeMeshes();

	// initialize the texture collection
	for (int i = 0; i < 16; i++)
	{
		m_textureIDs[i].tag = "/0";
		m_textureIDs[i].ID = -1;
	}
	m_loadedTextures = 0;
}

/***********************************************************
 *  ~SceneManager()
 *
 *  The destructor for the class
 ***********************************************************/
SceneManager::~SceneManager()
{
	// free the allocated objects
	m_pShaderManager = NULL;
	if (NULL != m_basicMeshes)
	{
		delete m_basicMeshes;
		m_basicMeshes = NULL;
	}

	// free the allocated OpenGL textures
	DestroyGLTextures();
}


/***********************************************************
 *  CreateGLTexture()
 *
 *  This method is used for loading textures from image files,
 *  configuring the texture mapping parameters in OpenGL, 
 *  generating the mipmaps, and loading the read texture into
 *  the next available texture slot in memory.
 ***********************************************************/
bool SceneManager::CreateGLTexture(const char* filename, std::string tag)
{
	int width = 0;
	int height = 0;
	int colorChannels = 0;
	GLuint textureID = 0;

	// indicate to always flip images vertically when loaded
	// OpenGL expects the 0.0 coordinate on the y-axis to be on the bottom side of the image, but images usually have 0.0 at the top of the y-axis
	stbi_set_flip_vertically_on_load(true);

	// try to parse the image data from the specified image file
	/*
	The function first takes as input the location of an image file. 
	It expects three ints as its second to fourth argument that stb_image.h will fill with the resulting image's width, height and number of color channels. 
	*/
	unsigned char* image = stbi_load(
		filename, 
		&width, 
		&height, 
		&colorChannels, 
		0);

	// if the image was successfully read from the image file
	if (image)
	{
		std::cout << "Successfully loaded image:" << filename << ", width:" << width << ", height:" << height << ", channels:" << colorChannels << std::endl;

		// reference textures with an ID;
		// The glGenTextures function first takes as input how many textures we want to generate and stores them in a unsigned int array.
		glGenTextures(1, &textureID);
		// bind it so any subsequent texture commands will configure the currently bound texture
		glBindTexture(GL_TEXTURE_2D, textureID);


		// set the texture wrapping parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		// set texture filtering parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);



		// start generating a texture using the previously loaded image data:
		// The first argument specifies the texture target;
		// 1 - setting this to GL_TEXTURE_2D means this operation will generate a texture on the currently bound texture object at the same target.
		// 2 - The second argument specifies the mipmap level for which we want to create a texture for if you want to set each mipmap level manually.
		// 3 - The third argument tells OpenGL in what kind of format we want to store the texture.
		//		Our image has only RGB values so we'll store the texture with RGB values as well.
		// 4 - The 4th and 5th argument sets the width and height of the resulting texture.
		//		We stored those earlier when loading the image so we'll use the corresponding variables.
		// 6 - should always be 0
		// 7 - The 7th and 8th argument specify the format and datatype of the source image.
		//		We loaded the image with RGB values and stored them as chars (bytes) so we'll pass in the corresponding values.
		// 9 - The actual image data


		// if the loaded image is in RGB format
		if (colorChannels == 3)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
		// if the loaded image is in RGBA format - it supports transparency
		else if (colorChannels == 4)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

		// Once glTexImage2D is called, the currently bound texture object now has the texture image attached to it.

		else
		{
			std::cout << "Not implemented to handle image with " << colorChannels << " channels" << std::endl;
			return false;
		}

		// call glGenerateMipmap after generating the texture. This will automatically generate all the required mipmaps for the currently bound texture.
		// generate the texture mipmaps for mapping textures to lower resolutions
		glGenerateMipmap(GL_TEXTURE_2D);

		// free the image data from local memory
		stbi_image_free(image);
		glBindTexture(GL_TEXTURE_2D, 0); // Unbind the texture

		// register the loaded texture and associate it with the special tag string
		m_textureIDs[m_loadedTextures].ID = textureID;
		m_textureIDs[m_loadedTextures].tag = tag;
		m_loadedTextures++;

		return true;
	}

	std::cout << "Could not load image:" << filename << std::endl;

	// Error loading the image
	return false;
}



/***********************************************************
 *  BindGLTextures()
 *
 *  This method is used for binding the loaded textures to
 *  OpenGL texture memory slots.  There are up to 16 slots.
 ***********************************************************/
void SceneManager::BindGLTextures()
{
	for (int i = 0; i < m_loadedTextures; i++)
	{
		// bind textures on corresponding texture units
		// activate the texture unit first before binding texture
		glActiveTexture(GL_TEXTURE0 + i);
		glBindTexture(GL_TEXTURE_2D, m_textureIDs[i].ID);
		std::cout << "Texture loaded from image! Texture ID: " << m_textureIDs[i].ID << std::endl;
	}
}


/***********************************************************
 *  DestroyGLTextures()
 *
 *  This method is used for freeing the memory in all the 
 *  used texture memory slots.
 ***********************************************************/
void SceneManager::DestroyGLTextures()
{
	for (int i = 0; i < m_loadedTextures; i++)
	{
		glGenTextures(1, &m_textureIDs[i].ID);
	}
}


/***********************************************************
 *  FindTextureID()
 *
 *  This method is used for getting an ID for the previously
 *  loaded texture bitmap associated with the passed in tag.
 ***********************************************************/
int SceneManager::FindTextureID(std::string tag)
{
	int textureID = -1;
	int index = 0;
	bool bFound = false;

	while ((index < m_loadedTextures) && (bFound == false))
	{
		if (m_textureIDs[index].tag.compare(tag) == 0)
		{
			textureID = m_textureIDs[index].ID;
			bFound = true;
		}
		else
			index++;
	}
	
	return(textureID);
}


/***********************************************************
 *  FindTextureSlot()
 *
 *  This method is used for getting a slot index for the previously
 *  loaded texture bitmap associated with the passed in tag.
 ***********************************************************/
int SceneManager::FindTextureSlot(std::string tag)
{
	int textureSlot = -1;
	int index = 0;
	bool bFound = false;

	while ((index < m_loadedTextures) && (bFound == false))
	{
		if (m_textureIDs[index].tag.compare(tag) == 0)
		{
			textureSlot = index;
			bFound = true;
		}
		else
			index++;
	}

	return(textureSlot);
}


/***********************************************************
 *  FindMaterial()
 *
 *  This method is used for getting a material from the previously
 *  defined materials list that is associated with the passed in tag.
 ***********************************************************/
bool SceneManager::FindMaterial(std::string tag, OBJECT_MATERIAL &material)
{
	if (m_objectMaterials.size() == 0)
	{
		return(false);
	}

	int index = 0;
	bool bFound = false;
	while ((index < m_objectMaterials.size()) && (bFound == false))
	{
		if (m_objectMaterials[index].tag.compare(tag) == 0)
		{
			bFound = true;
			material.ambientColor = m_objectMaterials[index].ambientColor;
			material.ambientStrength = m_objectMaterials[index].ambientStrength;
			material.diffuseColor = m_objectMaterials[index].diffuseColor;
			material.specularColor = m_objectMaterials[index].specularColor;
			material.shininess = m_objectMaterials[index].shininess;
		}
		else
		{
			index++;
		}
	}

	return(true);
}


/***********************************************************
 *  SetTransformations()
 *  This method is used for setting the transform buffer
 *  using the passed in transformation values.
 ***********************************************************/
void SceneManager::SetTransformations(
	glm::vec3 scaleXYZ,
	float XrotationDegrees,
	float YrotationDegrees,
	float ZrotationDegrees,
	glm::vec3 positionXYZ)
{
	// variables for this method
	glm::mat4 modelView;
	glm::mat4 scale;
	glm::mat4 rotationX;
	glm::mat4 rotationY;
	glm::mat4 rotationZ;
	glm::mat4 translation;

	// set the scale value in the transform buffer
	scale = glm::scale(scaleXYZ);
	// set the rotation values in the transform buffer
	rotationX = glm::rotate(glm::radians(XrotationDegrees), glm::vec3(1.0f, 0.0f, 0.0f));
	rotationY = glm::rotate(glm::radians(YrotationDegrees), glm::vec3(0.0f, 1.0f, 0.0f));
	rotationZ = glm::rotate(glm::radians(ZrotationDegrees), glm::vec3(0.0f, 0.0f, 1.0f));
	// set the translation value in the transform buffer
	translation = glm::translate(positionXYZ);

	modelView = translation * rotationX * rotationY * rotationZ * scale;

	if (NULL != m_pShaderManager)
	{
		m_pShaderManager->setMat4Value(g_ModelName, modelView);
	}
}


/***********************************************************
 *  SetShaderColor()
 *  This method is used for setting the passed in color
 *  into the shader for the next draw command
 ***********************************************************/
void SceneManager::SetShaderColor(
	float redColorValue,
	float greenColorValue,
	float blueColorValue,
	float alphaValue)
{
	// variables for this method
	glm::vec4 currentColor;

	currentColor.r = redColorValue;
	currentColor.g = greenColorValue;
	currentColor.b = blueColorValue;
	currentColor.a = alphaValue;

	if (NULL != m_pShaderManager)
	{
		m_pShaderManager->setIntValue(g_UseTextureName, false);
		m_pShaderManager->setVec4Value(g_ColorValueName, currentColor);
	}
}


/***********************************************************
 *  SetShaderTexture()
 *  This method is used for setting the texture data
 *  associated with the passed in ID into the shader.
 ***********************************************************/
void SceneManager::SetShaderTexture(
	std::string textureTag)
{
	if (NULL != m_pShaderManager)
	{
		m_pShaderManager->setIntValue(g_UseTextureName, true);

		int textureID = -1;
		textureID = FindTextureSlot(textureTag);
		m_pShaderManager->setSampler2DValue(g_TextureValueName, textureID);
	}
}


/***********************************************************
 *  SetTextureUVScale()
 *  This method is used for setting the texture UV scale
 *  values into the shader.
 ***********************************************************/
void SceneManager::SetTextureUVScale(float u, float v)
{
	if (NULL != m_pShaderManager)
	{
		m_pShaderManager->setVec2Value("UVscale", glm::vec2(u, v));
	}
}


/***********************************************************
 *  SetShaderMaterial()
 *  This method is used for passing the material values
 *  into the shader.
 ***********************************************************/
void SceneManager::SetShaderMaterial(
	std::string materialTag)
{
	if (m_objectMaterials.size() > 0)
	{
		OBJECT_MATERIAL material;
		bool bReturn = false;

		bReturn = FindMaterial(materialTag, material);
		if (bReturn == true)
		{
			m_pShaderManager->setVec3Value("material.ambientColor", material.ambientColor);
			m_pShaderManager->setFloatValue("material.ambientStrength", material.ambientStrength);
			m_pShaderManager->setVec3Value("material.diffuseColor", material.diffuseColor);
			m_pShaderManager->setVec3Value("material.specularColor", material.specularColor);
			m_pShaderManager->setFloatValue("material.shininess", material.shininess);
		}
	}
}


		/**************************************************************/
		/*** The code in the methods BELOW is for preparing and     ***/
		/*** rendering the 3D replicated scenes.                    ***/
		/**************************************************************/

/***********************************************************
 *  LoadSceneTextures()
 *  This method is used for preparing the 3D scene by loading
 *  the shapes, textures in memory to support the 3D scene rendering
 ***********************************************************/
void SceneManager::LoadSceneTextures()
{
	bool bReturn = false;

	// load the texture image files for the textures applied to objects in the 3D scene

	// Load the texture for the wall backdrop
	bReturn = CreateGLTexture("Textures/My_Tiles.jpg", "MyTiles");
	// Load the texture for the table
	bReturn = CreateGLTexture("Textures/My_Plane.jpg", "MyPlane");
	// Load the texture for the glass pitcher
	bReturn = CreateGLTexture("Textures/pitcher.jpg", "Pitcher");
	// Load the texture for the book
	bReturn = CreateGLTexture("Textures/book.jpg", "Book");
	bReturn = CreateGLTexture("Textures/bookCopy.jpg", "BookCopy");
	// Load the texture for the orange
	bReturn = CreateGLTexture("Textures/orange.jpg", "Orange");
	// Load the texture for the book name
	bReturn = CreateGLTexture("Textures/CS330.png", "CS330");
	// Load the texture for the pencil
	bReturn = CreateGLTexture("Textures/Pencil_body.jpg", "Pencil_body");
	// Load the texture for the pencil tip
	bReturn = CreateGLTexture("Textures/Pencil_tip.jpg", "Pencil_tip");

	// after the texture image data is loaded into memory, the
	// loaded textures need to be bound to texture slots - there
	// are a total of 16 available slots for scene textures
	BindGLTextures();
}


/***********************************************************
 *  DefineObjectMaterials()
 *  This method is used for configuring the various material
 *  settings for all of the objects within the 3D scene.
 ***********************************************************/
void SceneManager::DefineObjectMaterials()
{
	/*** Defining object materials. ***/

	// 1. Set the properties for the base plane material
	OBJECT_MATERIAL tableMaterial;
	tableMaterial.ambientColor = glm::vec3(0.1f, 0.1f, 0.1f);		// Controls how much of the ambient light color the material reflects
	tableMaterial.ambientStrength = 0.2f;							// Controls how much ambient light is reflected by the material
	tableMaterial.diffuseColor = glm::vec3(0.3f, 0.3f, 0.3f);		// Controls the color of the material when it is lit by a light source
	tableMaterial.specularColor = glm::vec3(0.1f, 0.1f, 0.1f);		// Controls the color of the material when it reflects light
	tableMaterial.shininess = 0.3;									// Controls the shininess of the material, affecting the size and intensity of highlights
	tableMaterial.tag = "MyTable";									// Set the tag for the material so it can be referenced later
	m_objectMaterials.push_back(tableMaterial);						// Add the material to the collection of materials

	// 2. Set the properties for the pitcher material
	OBJECT_MATERIAL glassMaterial;
	glassMaterial.ambientColor = glm::vec3(0.4f, 0.4f, 0.4f);
	glassMaterial.ambientStrength = 0.3f;
	glassMaterial.diffuseColor = glm::vec3(0.3f, 0.3f, 0.3f);
	glassMaterial.specularColor = glm::vec3(0.6f, 0.6f, 0.6f);
	glassMaterial.shininess = 85.0;
	glassMaterial.tag = "glass";
	// Add the material to the collection of materials
	m_objectMaterials.push_back(glassMaterial);

	// 3. Set the properties for the background/backdrop material
	OBJECT_MATERIAL backdropMaterial;
	backdropMaterial.ambientColor = glm::vec3(0.3f, 0.3f, 0.3f);	// Controls how much of the ambient light color the material reflects
	backdropMaterial.ambientStrength = 0.3f;						// Controls how much ambient light is reflected by the material
	backdropMaterial.diffuseColor = glm::vec3(0.6f, 0.5f, 0.1f);	// Controls the color of the material when it is lit by a light source
	backdropMaterial.specularColor = glm::vec3(0.0f, 0.0f, 0.0f);	// Controls the color of the material when it reflects light
	backdropMaterial.shininess = 0.0;								// Controls the shininess of the material, affecting the size and intensity of highlights
	backdropMaterial.tag = "MyBackground";							// Set the tag for the material so it can be referenced later
	m_objectMaterials.push_back(backdropMaterial);					// Add the material to the collection of materials

	// 4. Set the properties for the orange and material
	OBJECT_MATERIAL orangeMaterial;
	orangeMaterial.ambientColor = glm::vec3(0.25f, 0.15f, 0.05f);	// Warm orange tone for ambient bounce
	orangeMaterial.ambientStrength = 0.3f;
	orangeMaterial.diffuseColor = glm::vec3(0.9f, 0.4f, 0.1f);		// Vibrant orange under light
	orangeMaterial.specularColor = glm::vec3(0.05f, 0.02f, 0.01f);	// Very subtle highlights
	orangeMaterial.shininess = 2.0f;								// Low shininess for a rough surface
	orangeMaterial.tag = "MyOrange";
	m_objectMaterials.push_back(orangeMaterial);

	// 5. Set the properties for the pencil material
	OBJECT_MATERIAL pencilMaterial;
	pencilMaterial.ambientColor = glm::vec3(0.3f, 0.3f, 0.1f);		// Yellow ambient tone
	pencilMaterial.ambientStrength = 0.35f;
	pencilMaterial.diffuseColor = glm::vec3(1.0f, 0.9f, 0.2f);		// Bright yellow for lighting response
	pencilMaterial.specularColor = glm::vec3(0.2f, 0.2f, 0.1f);		// Subtle shininess for paint
	pencilMaterial.shininess = 8.0f;								// Moderate shininess for semi-gloss surface
	pencilMaterial.tag = "MyPencil";
	m_objectMaterials.push_back(pencilMaterial);
}


/***********************************************************
 *  SetupSceneLights()
 *  This method is called to add and configure the light 
 *  sources for the 3D scene.  There are up to 4 light sources.
 ***********************************************************/
void SceneManager::SetupSceneLights()
{
	/*** First light Source: Sets the position of the main illumination light source slightly to the left and above the objects ***/
	// Left Key Light: Acts as a key light, giving primary form and shape to objects from the left side

	m_pShaderManager->setVec3Value("lightSources[0].position", -3.0f, 4.0f, 6.0f);			// Position the source of light relative to objects
	m_pShaderManager->setVec3Value("lightSources[0].ambientColor", 0.01f, 0.01f, 0.01f);	// The ambient light color to illuminate the scene even when no light is directly hitting the surface.
	m_pShaderManager->setVec3Value("lightSources[0].diffuseColor", 0.5f, 0.5f, 0.5f);		// The diffuse light color simulates directional lighting, where the surface facing the light is illuminated.
	m_pShaderManager->setVec3Value("lightSources[0].specularColor", 0.2f, 0.2f, 0.2f);		// The specular light color simulates the reflection of light on shiny surfaces, creating highlights.
	m_pShaderManager->setFloatValue("lightSources[0].focalStrength", 32.0f);				// The focal strength controls the intensity of the light source, affecting how much light is emitted.
	m_pShaderManager->setFloatValue("lightSources[0].specularIntensity", 0.2f);				// The specular intensity controls the strength of the specular highlights on surfaces.

	/*** Second light Source: Sets the position of the main illumination light source slightly to the right and above the objects ***/
	// Right Key Light: Acts as symetrical key light, giving primary form and shape to objects from the right side
	m_pShaderManager->setVec3Value("lightSources[1].position", 3.0f, 4.0f, 6.0f);
	m_pShaderManager->setVec3Value("lightSources[1].ambientColor", 0.01f, 0.01f, 0.01f);
	m_pShaderManager->setVec3Value("lightSources[1].diffuseColor", 0.5f, 0.5f, 0.5f);
	m_pShaderManager->setVec3Value("lightSources[1].specularColor", 0.2f, 0.2f, 0.2f);
	m_pShaderManager->setFloatValue("lightSources[1].focalStrength", 32.0f);
	m_pShaderManager->setFloatValue("lightSources[1].specularIntensity", 0.2f);

	/*** Third light Source: Sets the position of the main illumination light source in front of the scene ***/
	// Front Key Light: Acts as a spot light, to fill ambient light and brighten and balance the scene
	m_pShaderManager->setVec3Value("lightSources[2].position", 0.0f, 3.0f, 20.0f);
	m_pShaderManager->setVec3Value("lightSources[2].ambientColor", 0.2f, 0.2f, 0.2f);
	m_pShaderManager->setVec3Value("lightSources[2].diffuseColor", 0.8f, 0.8f, 0.8f);
	m_pShaderManager->setVec3Value("lightSources[2].specularColor", 0.0f, 0.0f, 0.0f);
	m_pShaderManager->setFloatValue("lightSources[2].focalStrength", 12.0f);
	m_pShaderManager->setFloatValue("lightSources[2].specularIntensity", 0.2f);

	// Update the shader with the predefined number of light sources
	m_pShaderManager->setBoolValue(g_UseLightingName, true);
}


/***********************************************************
 *  PrepareScene()
 *  This method is used for preparing the 3D scene by loading
 *  the shapes, textures in memory to support the 3D scene rendering
 ***********************************************************/
void SceneManager::PrepareScene()
{
	// load the texture image files for the textures applied to objects in the 3D scene
	LoadSceneTextures();

	// define the materials that will be used for the objects in the 3D scene
	DefineObjectMaterials();

	// add and defile the light sources for the 3D scene
	SetupSceneLights();

	// load the needed meshes in memory to rendered 3D scene
	m_basicMeshes->LoadBoxMesh();		// To be used for the table
	m_basicMeshes->LoadPlaneMesh();		// To be used for the backdrop
	m_basicMeshes->LoadCylinderMesh();	// To be used for the juice pitcher neck, body, and the pencil body
	m_basicMeshes->LoadSphereMesh();	// To be used for the juice pitcher top and bottom
	m_basicMeshes->LoadTorusMesh();		// To be used for the juice pitcher lip
	m_basicMeshes->LoadConeMesh();		// To be used for the pencil tip
}


/***********************************************************
 *  RenderScene()
 *  This method is used for rendering the 3D scene by 
 *  transforming and drawing the basic 3D shapes from ShapeMeshes.cpp
 ***********************************************************/
void SceneManager::RenderScene()
{
	// set the shader program to be used for rendering the scene
	RenderTable();
	RenderBackdrop();
	RenderJuicePitcher();
	RenderBook();
	RenderOrange();
	RenderPencil();
}


/***********************************************************
 *  RenderTable()
 *  This method is called to render the shapes for the table object.
 ***********************************************************/
void SceneManager::RenderTable()
{
	// declare the variables for the transformations
	glm::vec3 scaleXYZ;
	float XrotationDegrees = 0.0f;
	float YrotationDegrees = 0.0f;
	float ZrotationDegrees = 0.0f;
	glm::vec3 positionXYZ;

	/*** Set needed transformations before drawing the basic mesh for the table plane***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(34.0f, .6f, 12.0f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(0.0f, 0.2f, -0.9f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the optional plain shader color for the table mesh
	// SetShaderColor(1, 1, 1, 1);
	
	// Set the shader texture for the table mesh. Call the texture by its tag
	SetShaderTexture("MyPlane");
	// Set the UV scale for the texture mapping
	SetTextureUVScale(1.0, 1.0);
	// Set the shader material for the table mesh
	SetShaderMaterial("MyTable");
	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawBoxMesh();
}


/***********************************************************
 *  RenderBackdrop()
 *  This method is called to render the shapes for the scene background object.
 ***********************************************************/
void SceneManager::RenderBackdrop()
{
	// declare the variables for the transformations
	glm::vec3 scaleXYZ;
	float XrotationDegrees = 0.0f;
	float YrotationDegrees = 0.0f;
	float ZrotationDegrees = 0.0f;
	glm::vec3 positionXYZ;

	/*** Set needed transformations before drawing the basic mesh ***/

	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(20.0f, -0.5f, 20.0f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 90.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(0.0f, 15.0f, -8.0f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the optional plain shader color for the table mesh
	// SetShaderColor(1, 1, 1, 1);

	// Set the shader texture for the backdground mesh
	SetShaderTexture("MyTiles");
	// Set the UV scale for the texture mapping
	SetTextureUVScale(1.0, 1.0);
	// Set the shader color for the background mesh
	SetShaderMaterial("MyBackground");
	// draw the mesh with transformation values - this plane is used for the backdrop
	m_basicMeshes->DrawPlaneMesh();
}


/***************************************************************************
 *  RenderJuicePitcher()
 *  This method is called to render the shapes for the Juice Pitcher object.
 *	Parts setup from top to bottom:
 ***************************************************************************/
void SceneManager::RenderJuicePitcher()
{
	// declare the variables for the transformations
	glm::vec3 scaleXYZ;
	float XrotationDegrees = 0.0f;
	float YrotationDegrees = 0.0f;
	float ZrotationDegrees = 0.0f;
	glm::vec3 positionXYZ;
	
	// Set needed transformations before drawing the basic torus mesh
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(.65f, .9f, .65f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = -90.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(3.8f, 6.8f, 0.25f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the shader texture for the pitcher mesh
	SetShaderTexture("Pitcher");
	// Set the UV scale for the texture mapping to modify how the texture is scaled across the surface.
	SetTextureUVScale(1.0, 1.0);

	// Set the shader color for the mesh
	// SetShaderColor(.91, 0.89, .90, 0.7);

	// Set the shader material for the mesh
	SetShaderMaterial("glass");

	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawTorusMesh();


	/*** Set needed transformations for the pither neck half sphere ***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(.7f, .9f, .7f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 180.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(3.8f, 6.75f, 0.25f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the shader texture for the pitcher mesh
	SetShaderTexture("Pitcher");

	// Set the UV scale for the texture mapping to modify how the texture is scaled across the surface.
	SetTextureUVScale(1.0, 1.0);

	// Set the shader material for the mesh
	SetShaderMaterial("glass");

	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawHalfSphereMesh();


	/*** Set needed transformations for the pither neck cylinder sphere ***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(.5f, .65f, .5f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(3.8f, 5.65f, 0.25f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the shader texture for the pitcher mesh
	SetShaderTexture("Pitcher");
	// Set the UV scale for the texture mapping to modify how the texture is scaled across the surface.
	SetTextureUVScale(1.0, 1.0);

	// Set the shader material for the mesh
	SetShaderMaterial("glass");

	// draw the mesh with transformation values
	m_basicMeshes->DrawCylinderMesh(false, false, true);


	/*** Set needed transformations for the pither neck bottom half sphere ***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(.905f, .9f, .905f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = -6.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(3.8f, 4.9f, 0.25f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the shader texture for the pitcher mesh
	SetShaderTexture("Pitcher");
	// Set the UV scale for the texture mapping to modify how the texture is scaled across the surface.
	SetTextureUVScale(1.0, 1.0);

	// Set the shader material for the mesh
	SetShaderMaterial("glass");

	// draw the mesh with transformation values
	m_basicMeshes->DrawHalfSphereMesh();


	/*** Set needed transformations for the pither main body cylinder sphere ***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(.9f, 4.4f, .9f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	// positionXYZ = glm::vec3(1.8f, .9f, 0.25f);
	positionXYZ = glm::vec3(3.8f, .5f, 0.25f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the shader texture for the pitcher mesh
	SetShaderTexture("Pitcher");
	// Set the UV scale for the texture mapping to modify how the texture is scaled across the surface.
	SetTextureUVScale(1.0, 1.0);

	// Set the shader material for the mesh
	SetShaderMaterial("glass");

	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawCylinderMesh(false, false, true);



	/*** Set needed transformations for the pither bottom half cylinder sphere ***/

	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(.95f, .4f, .95f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(3.8f, 0.5f, 0.25f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the shader texture for the pitcher mesh
	SetShaderTexture("Pitcher");
	// Set the UV scale for the texture mapping to modify how the texture is scaled across the surface.
	SetTextureUVScale(1.0, 1.0);

	// Set the shader color for the mesh
	// SetShaderColor(.91, 0.89, .90, 0.7);

	// Set the shader material for the mesh
	SetShaderMaterial("glass");

	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawHalfSphereMesh();

	/***************************************************************************
	*	End of the Juice Pitcher rendering RenderJuicePitcher()
	***************************************************************************/
}


/***********************************************************
 *  RenderBook()
 *  This method is called to render the shapes for the book object.
 ***********************************************************/
void SceneManager::RenderBook()
{
	// declare the variables for the transformations
	glm::vec3 scaleXYZ;
	float XrotationDegrees = 0.0f;
	float YrotationDegrees = 0.0f;
	float ZrotationDegrees = 0.0f;
	glm::vec3 positionXYZ;

	/*** Set needed transformations before drawing the basic mesh for the book ***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(5.0f, 1.0f, 5.0f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = 30.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(0.0f, 1.0f, 1.2f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	
	// Set the shader texture for the book mesh. Call the texture by its tag
	// SetShaderTexture("Book");
	// Set the shader texture for the book name.
	SetShaderTexture("CS330");

	// Set the UV scale for the texture mapping
	SetTextureUVScale(1.0, 1.0);

	// Set the shader material for the table mesh
	SetShaderMaterial("MyTable");

	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawBoxMesh();


	/*** Set needed transformations before drawing the basic mesh for the book pages front ***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(4.8f, 0.8f, 0.05f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = 30.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(1.32f, 1.0f, 3.30f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the optional plain shader color for the book mesh
	SetShaderColor(1, 1, 1, 0.5);

	SetShaderMaterial("glass");
	// draw the mesh with transformation values
	m_basicMeshes->DrawBoxMesh();


	/*** Set needed transformations before drawing the basic mesh for the book pages back ***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(4.8f, 0.8f, 0.05f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = 30.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(-1.146f, 1.0f, -1.0f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the optional plain shader color for the table mesh
	SetShaderColor(1, 1, 1, 0.5);

	SetShaderMaterial("glass");

	// draw the mesh with transformation values
	m_basicMeshes->DrawBoxMesh();


	/*** Set needed transformations before drawing the basic mesh for the book pages side ***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(4.96f, 0.8f, 0.1f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = -60.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(2.15f, 1.0f, -0.02f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the optional plain shader color for the table mesh
	SetShaderColor(1, 1, 1, 0.5);

	SetShaderMaterial("glass");
	// draw the mesh with transformation values
	m_basicMeshes->DrawBoxMesh();
}

/***********************************************************
 *  RenderOrange()
 *  This method is called to render the shapes for the orange object.
 ***********************************************************/
void SceneManager::RenderOrange()
{
	// declare the variables for the transformations
	glm::vec3 scaleXYZ;
	float XrotationDegrees = 0.0f;
	float YrotationDegrees = 0.0f;
	float ZrotationDegrees = 0.0f;
	glm::vec3 positionXYZ;

	/*** Set needed transformations before drawing the basic mesh for the table plane***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(0.9f, 0.9f, 0.9f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 0.0f;
	YrotationDegrees = 45.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(0.0f, 2.4f, -1.0f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the shader texture for the table mesh. Call the texture by its tag
	SetShaderTexture("Orange");

	// Set the UV scale for the texture mapping
	SetTextureUVScale(1.0, 1.0);

	// Set the shader material for the table mesh
	SetShaderMaterial("MyOrange");

	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawSphereMesh();
}


/***********************************************************
 *  RenderPencil()
 *  This method is called to render the shapes for the orange object.
 ***********************************************************/
void SceneManager::RenderPencil()
{
	// declare the variables for the transformations
	glm::vec3 scaleXYZ;
	float XrotationDegrees = 0.0f;
	float YrotationDegrees = 0.0f;
	float ZrotationDegrees = 0.0f;
	glm::vec3 positionXYZ;


	/*** Set needed transformations before drawing the basic mesh for the table plane***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(0.15f, 2.2f, 0.15f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = 90.0f;
	YrotationDegrees = 25.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(0.0f, 1.65f, 1.2f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);
	
	// Set the shader texture for the table mesh. Call the texture by its tag
	SetShaderTexture("Pencil_body");

	// Set the UV scale for the texture mapping
	SetTextureUVScale(6.0, 1.0);

	// Set the shader material for the table mesh
	// SetShaderMaterial("MyPencil");

	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawCylinderMesh();


	// Draw the pencil tip

	/*** Set needed transformations before drawing the basic mesh for the table plane***/
	// set the XYZ scale for the mesh
	scaleXYZ = glm::vec3(0.15f, 0.8f, 0.15f);

	// set the XYZ rotation for the mesh
	XrotationDegrees = -90.0f;
	YrotationDegrees = 30.0f;
	ZrotationDegrees = 0.0f;

	// set the XYZ position for the mesh
	positionXYZ = glm::vec3(0.0f, 1.65f, 1.2f);

	// set the transformations into memory to be used on the drawn meshes
	SetTransformations(
		scaleXYZ,
		XrotationDegrees,
		YrotationDegrees,
		ZrotationDegrees,
		positionXYZ);

	// Set the shader texture for the table mesh. Call the texture by its tag
	SetShaderTexture("Pencil_tip");

	// Set the UV scale for the texture mapping
	SetTextureUVScale(1.0, 1.0);

	// Set the shader material for the table mesh
	// SetShaderMaterial("MyPencil");

	// draw the mesh with transformation values - this plane is used for the base
	m_basicMeshes->DrawConeMesh();
}
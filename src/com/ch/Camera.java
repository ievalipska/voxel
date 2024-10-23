package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Represents a camera in a 3D scene and provides functionality for calculating
 * projection and view matrices based on its position and orientation. It also includes
 * methods for adjusting to a viewport and calculating a projection matrix based on
 * camera-specific data.
 */
public abstract class Camera {

	protected Matrix4f projection;
	protected Matrix4f viewProjectionMat4;
	protected CameraStruct values;
	protected Transform transform;

	protected Camera(Matrix4f projection) {
		this.projection = projection;
		transform = new Transform();
	}

	/**
	 * Returns a 4x4 matrix representing the combined view and projection transformations.
	 * It recalculates the view matrix if the `transform` has changed or if the
	 * `viewProjectionMat4` is null.
	 *
	 * @returns a 4x4 matrix representing the combined view and projection transformation.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}
		return viewProjectionMat4;
	}

	/**
	 * Calculates the view matrix by combining camera rotation, translation, and a
	 * predefined projection matrix. It returns the resulting matrix, also storing it in
	 * the `viewProjectionMat4` variable.
	 *
	 * @returns a 4x4 matrix representing the combined view projection transformation.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}

	/**
	 * Calculates a translation matrix based on the negative of the camera position.
	 * It returns a 4x4 matrix initialized with the translation values.
	 *
	 * @returns a 4x4 translation matrix representing the camera position in 3D space.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns the value of the `transform` variable, providing access to its contents.
	 * The function does not modify the `transform` variable; it only allows its retrieval.
	 * The `transform` variable is assumed to be a valid object of type `Transform`.
	 *
	 * @returns an object of type `Transform`.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Defines a base class for camera-related data structures, providing a method to
	 * retrieve the data as a Matrix4f object.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}

package java.game.tetrominoes.enums;

/**
 * 旋转角度。
 */
public enum RotationDegree {
    Degree0, Degree90, Degree180, Degree270;

    /**
     * 旋转一个角度。
     * 
     * @param rotationDirection
     *            旋转方向
     * @return 旋转结果的角度
     */
    public RotationDegree rotate(final RotationDirection rotationDirection) {
	switch (rotationDirection) {
	case Left:
	    return rotateLeft();
	case Right:
	    return rotateRight();
	default:
	    return this;
	}
    }

    private RotationDegree rotateRight() {
	if (this == Degree0) {
	    return Degree270;
	}
	return RotationDegree.values()[ordinal() - 1];
    }

    private RotationDegree rotateLeft() {
	if (this == Degree270) {
	    return Degree0;
	}
	return RotationDegree.values()[ordinal() + 1];
    }
}
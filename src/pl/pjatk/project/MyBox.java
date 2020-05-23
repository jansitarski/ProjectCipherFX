package pl.pjatk.project;

import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class MyBox extends Box {
    public MyBox(double width, double height, double depth) {
        super(width, height, depth);
        setTranslateX(0);
        setTranslateY(0);
        setTranslateZ(0);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("SplashScreenLogo.png")));
        setMaterial(material);

        //Rotate rotate = new Rotate();
        //rotate.setAngle(20);
        //getTransforms().addAll(rotate);


        PointLight light = new PointLight();
        light.setTranslateX(250);
        light.setTranslateY(100);
        light.setTranslateZ(300);

        rotate(this);
    }

    private void rotate(Node node) {
        RotateTransition rotate = new RotateTransition();

        rotate.setAxis(Rotate.Y_AXIS);

        rotate.setByAngle(360);

        rotate.setCycleCount(500);

        rotate.setDuration(Duration.millis(5000));

        rotate.setAutoReverse(true);

        rotate.setNode(node);

        rotate.play();
    }
}

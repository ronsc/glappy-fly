package sut.cpe.edp.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import sut.cpe.edp.core.Glappy;

public class GlappyJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    PlayN.run(new Glappy());
  }
}

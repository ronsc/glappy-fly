package sut.cpe.edp.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import sut.cpe.edp.core.Glappy;

public class GlappyActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new Glappy());
  }
}

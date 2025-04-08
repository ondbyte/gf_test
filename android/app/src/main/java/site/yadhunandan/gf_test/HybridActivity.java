package site.yadhunandan.gf_test;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import org.godotengine.godot.GodotFragment;

import io.flutter.embedding.android.FlutterFragment;
import site.yadhunandan.gf_test.GodotFragmentExt;

public class HybridActivity extends FragmentActivity {
    private static final String FLUTTER_TAG = "flutter_fragment";
    private static Integer FlutterViewId = View.generateViewId();
    private static final String GODOT_TAG = "godot_fragment";
    private GodotFragment godotFragment;

    private FlutterFragment createFlutterFragment(){
        // Add a container for Flutter
        FrameLayout flutterContainer = new FrameLayout(this);
        flutterContainer.setId(FlutterViewId);
        flutterContainer.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Add flutter container to the root view
        addContentView(flutterContainer, flutterContainer.getLayoutParams());

        // Create & attach Flutter fragment manually
        var ff = FlutterFragment
                .withNewEngine() 
                .initialRoute("/")
                .build();
        return  ff;
    }

    private void addFlutter(FlutterFragment ff){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(FlutterViewId, ff, FLUTTER_TAG);
        tx.commitNow();
    }

    private void addGodot(FlutterFragment ff){
        FrameLayout godotContainer = new FrameLayout(this);
        int godotViewId = View.generateViewId();
        godotContainer.setId(godotViewId);
        godotContainer.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        addContentView(godotContainer, godotContainer.getLayoutParams());

        godotContainer.post(() -> {
            //godotFragment = new GodotFragmentExt(ff);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction tx = fm.beginTransaction();
            tx.replace(godotViewId, godotFragment, GODOT_TAG);
            tx.commitNow();
        });
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_hybrid); // 🔥 this is the key!

        FlutterFragment flutterFragment = FlutterFragment
                .withNewEngine()
                .initialRoute("/")
                .build();

        // Add Flutter first so Godot can access the engine
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flutter_container, flutterFragment, FLUTTER_TAG)
                .commitNow();


        GodotFragment godotFragment = new GodotFragmentExt(flutterFragment);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.godot_container, godotFragment, GODOT_TAG)
                    .commitNow();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


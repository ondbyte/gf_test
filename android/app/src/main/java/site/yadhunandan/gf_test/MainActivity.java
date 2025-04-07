package site.yadhunandan.gf_test;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.flutter.embedding.android.FlutterFragment;
import io.flutter.plugin.common.MethodChannel;

import org.godotengine.godot.Godot;
import org.godotengine.godot.GodotActivity;
import org.godotengine.godot.GodotFragment;
import org.godotengine.godot.GodotLib;
import org.godotengine.godot.plugin.GodotPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MainActivity extends GodotActivity {
    private static final String FLUTTER_TAG = "flutter_fragment";
    private static final String CHANNEL = "gf";

    private FlutterFragment flutterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();

        // Add a container for Flutter
        FrameLayout flutterContainer = new FrameLayout(this);
        flutterContainer.setId(View.generateViewId());
        flutterContainer.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        // Add flutter container to the root view
        addContentView(flutterContainer, flutterContainer.getLayoutParams());

        // Create & attach Flutter fragment manually
        flutterFragment = FlutterFragment
                .withNewEngine()
                .initialRoute("/")
                .build();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(flutterContainer.getId(), flutterFragment, FLUTTER_TAG);
        tx.commitNow();
    }

    @Override
    public List<String> getCommandLine() {
        return Arrays.asList("res://main.tscn");
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
    public Set<GodotPlugin> getHostPlugins(Godot engine) {
        var e = super.getHostPlugins(engine);
        var fe = flutterFragment.getFlutterEngine();
        if (fe==null){
            throw new RuntimeException("unable to find flutter engine");
        }
        e.add(new GfCommunication(engine, fe.getDartExecutor().getBinaryMessenger()));
        return e;
    }
}

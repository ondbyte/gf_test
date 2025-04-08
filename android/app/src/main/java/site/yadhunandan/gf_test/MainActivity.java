package site.yadhunandan.gf_test;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends GodotActivity {
    private static final String FLUTTER_TAG = "flutter_fragment";

    private FlutterFragment flutterFragment;

    @NonNull
    @Override
    protected GodotFragment initGodotInstance() {
        createFlutter();
        return super.initGodotInstance();
    }

    private void createFlutter(){
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
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
        Set<GodotPlugin> plugins = new HashSet<>();
        plugins.add(new GfCommunication(engine, flutterFragment));
        return plugins;
    }
}

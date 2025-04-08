package site.yadhunandan.gf_test;

import org.godotengine.godot.Godot;
import org.godotengine.godot.GodotFragment;
import org.godotengine.godot.plugin.GodotPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.flutter.embedding.android.FlutterFragment;
import io.flutter.plugin.common.BinaryMessenger;

public class GodotFragmentExt extends GodotFragment {
    private FlutterFragment ff;
    public GodotFragmentExt(FlutterFragment frag){
        super();
        ff = frag;
    }
    @Override
    public List<String> getCommandLine() {
        var e = super.getCommandLine();
        return Arrays.asList("res://main.tscn");
    }

    @Override
    public Set<GodotPlugin> getHostPlugins(Godot engine) {
        super.getHostPlugins(engine);
        Set<GodotPlugin> plugins = new HashSet<>();
        plugins.add(new GfCommunication(engine, ff));
        return plugins;
    }
}

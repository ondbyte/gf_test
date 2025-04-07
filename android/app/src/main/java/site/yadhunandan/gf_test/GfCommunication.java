package site.yadhunandan.gf_test;

import androidx.annotation.NonNull;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

public class GfCommunication extends GodotPlugin {
    private final HashMap<String,MethodChannel.Result> resultCache = new HashMap<>();
    private final String pluginName = "gf_plugin";

    public GfCommunication(Godot godot, BinaryMessenger messenger) {
        super(godot);
        new MethodChannel(
                messenger,pluginName
        ).setMethodCallHandler((call, result) -> {
            try{

                emitSignal(godot,pluginName,new SignalInfo("OnMsg", String.class,Object.class),call.method,call.arguments);
                resultCache.put(call.method, result);
            } catch (Exception e){
                result.error(pluginName+"/emit_signal_failed",e.getMessage(),null);
            }
        });
    }

    @NonNull
    @Override
    public String getPluginName() {
        return pluginName;
    }

    @UsedByGodot
    public void OnMsg(String method,Object arguments){
        var r = resultCache.remove(method);
        if (r!=null){
            r.success(arguments);
        }
    }
}

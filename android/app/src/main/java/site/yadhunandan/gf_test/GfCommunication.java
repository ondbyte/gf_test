package site.yadhunandan.gf_test;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.android.FlutterFragment;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

public class GfCommunication extends GodotPlugin {
    private final HashMap<String,MethodChannel.Result> resultCache = new HashMap<>();
    private final String pluginName = "gf_plugin";

    private FlutterFragment ff;

    public GfCommunication(Godot godot, FlutterFragment frag) {
        super(godot);
        ff = frag;
    }

    @Nullable
    @Override
    public View onMainCreate(Activity activity) {
        var e = ff.getFlutterEngine();
        if (e==null){
            throw new RuntimeException("no flutter engine");
        }
        new MethodChannel(
                e.getDartExecutor().getBinaryMessenger(),pluginName
        ).setMethodCallHandler((call, result) -> {
            try{
                emitSignal("OnMsg",call.method,call.arguments);
                resultCache.put(call.method, result);
            } catch (Exception err){
                result.error(pluginName+"/emit_signal_failed",err.getMessage(),null);
            }
        });
        return super.onMainCreate(activity);
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

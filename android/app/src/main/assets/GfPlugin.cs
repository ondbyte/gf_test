using Godot;
using System;
using Godot.Collections;

public partial class GfPlugin : Node
{
    private string pluginName = "gf_plugin";
    private GodotObject plugin;
    
    public GfPlugin()
    {
        plugin = Engine.GetSingleton(pluginName);
        if (plugin == null)
        {
            throw new Exception($"Can't find {pluginName} plugin");
        }
        var err = plugin.Connect("OnMsg",new Callable(this,nameof(OnMsg)));
    }

    private void OnMsg(string method, Variant args)
    {
        switch (method)
        {
            default:
            {
                Dictionary<string,Variant> res = new Dictionary<string, Variant>();
                res.Add("error","method "+method+" has no handler, this is a default response from godot");
                plugin.Call(new StringName("OnMsg"), new Variant[]{method,res});
                break;
            }
        }
    }
}

package aurora.bpm.script;

import java.util.HashMap;

import uncertain.composite.CompositeMap;
import aurora.javascript.Context;
import aurora.javascript.RhinoException;
import aurora.javascript.Script;
import aurora.javascript.Scriptable;
import aurora.javascript.ScriptableObject;
import aurora.javascript.Undefined;
import aurora.javascript.Wrapper;
import aurora.javascript.json.JsonParser;
import aurora.javascript.json.JsonParser.ParseException;
import aurora.plugin.script.engine.AuroraScriptEngine;
import aurora.plugin.script.engine.CompiledScriptCache;
import aurora.plugin.script.engine.InterruptException;
import aurora.plugin.script.scriptobject.CompositeMapObject;

public class BPMScriptEngine extends AuroraScriptEngine {
	public static final String DATA_OBJECT = "data_object";
	private HashMap<String, Object> localVariable = new HashMap<String, Object>();

	public BPMScriptEngine(CompositeMap context) {
		super(context);
	}

	public void registry(String name, Object obj) {
		localVariable.put(name, obj);
	}

	@Override
	protected void preDefine(Context cx, Scriptable scope) {
		super.preDefine(cx, scope);
		Object data_object = service_context.get("data");
		Scriptable ctx = cx.newObject(scope, CompositeMapObject.CLASS_NAME,
				new Object[] { service_context });
		ScriptableObject.defineProperty(scope, "$ctx", ctx,
				ScriptableObject.EMPTY);
		if (data_object == null) {
			String json = service_context.getString("$json", "{}");
			service_context.remove("$json");
			JsonParser parser = new JsonParser(cx, scope);
			try {
				data_object = (Scriptable) parser.parseValue(json);
				service_context.put("data", data_object);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ScriptableObject.defineProperty(scope, "$data", data_object,
				ScriptableObject.CONST);
		for (String key : localVariable.keySet())
			ScriptableObject.defineProperty(scope, key, localVariable.get(key),
					ScriptableObject.READONLY);
	}

	public Object eval(String source) throws Exception {
		Object ret = null;
		Context cx = Context.enter();
		try {
			cx.putThreadLocal(KEY_SERVICE_CONTEXT, service_context);
			Scriptable scope = cx.newObject(topLevel);
			scope.setParentScope(null);
			scope.setPrototype(topLevel);
			preDefine(cx, scope);
			// ScriptImportor.organizeUserImport(cx, scope, service_context);
			Script scr = CompiledScriptCache.getInstance().getScript(source,
					cx);
			ret = scr == null ? null : scr.exec(cx, scope);
		} catch (RhinoException re) {
			if (re.getCause() instanceof InterruptException)
				throw (InterruptException) re.getCause();
			throw re;
		} finally {
			Context.exit();
		}

		if (ret instanceof Wrapper) {
			ret = ((Wrapper) ret).unwrap();
		} else if (ret instanceof Undefined)
			ret = null;
		return ret;
	}

}
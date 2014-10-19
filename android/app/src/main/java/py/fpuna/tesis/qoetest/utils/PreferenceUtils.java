package py.fpuna.tesis.qoetest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import py.fpuna.tesis.qoetest.model.PerfilUsuario;

/**
 * Created by LF on 19/10/2014.
 */
public class PreferenceUtils {
    private Gson gson;
    private Context context;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    public PreferenceUtils(Context context){
        this.context = context;
        this.gson = new Gson();
        mPrefs = context.getSharedPreferences(Constants.SAHRED_PREFERENCES,
                Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    public PerfilUsuario getPerfilUsuario(){
        PerfilUsuario perfil = null;
        if(mPrefs.contains(Constants.PERFIL_USUARIO_SHARED)){
            perfil = gson.fromJson(mPrefs.getString(Constants.PERFIL_USUARIO_SHARED,""),
                    PerfilUsuario.class);

        }
        return perfil;
    }
}

package py.fpuna.tesis.qoetest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.model.PhoneInfo;

/**
 * Created by LF on 19/10/2014.
 */
public class PreferenceUtils {
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    private Gson gson;
    private Context context;

    public PreferenceUtils(Context context) {
        this.context = context;
        this.gson = new Gson();
        mPrefs = context.getSharedPreferences(Constants.SAHRED_PREFERENCES,
                Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    /**
     * @return
     */
    public PerfilUsuario getPerfilUsuario() {
        PerfilUsuario perfil = null;
        if (mPrefs.contains(Constants.PERFIL_USUARIO_SHARED)) {
            perfil = gson.fromJson(mPrefs.getString(Constants.PERFIL_USUARIO_SHARED, ""),
                    PerfilUsuario.class);

        }
        return perfil;
    }

    /**
     * @return
     */
    public PhoneInfo getDeviceInfo() {
        PhoneInfo info = null;
        if (mPrefs.contains(Constants.DEVICE_SHARED)) {
            info = gson.fromJson(mPrefs.getString(Constants.DEVICE_SHARED, ""),
                    PhoneInfo.class);
        }
        return info;
    }

    public void savePhoneInfo(PhoneInfo info) {
        mEditor.putString(Constants.DEVICE_SHARED, gson.toJson(info));
        mEditor.commit();
    }

    public void savePerfilUsuario(PerfilUsuario perfilUsuario) {
        mEditor.putString(Constants.PERFIL_USUARIO_SHARED, gson.toJson(perfilUsuario));
        mEditor.commit();
    }
}

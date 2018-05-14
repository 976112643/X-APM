package github.tornaco.xposedmoduletest.ui.tiles.app.per;

import android.content.Context;
import android.support.annotation.NonNull;

import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.xposed.app.XAshmanManager;
import github.tornaco.xposedmoduletest.xposed.bean.AppSettings;

/**
 * Created by guohao4 on 2018/1/15.
 * Email: Tornaco@163.com
 */

public class AppStartSetting extends AppSettingsSwitchTile {

    public AppStartSetting(@NonNull Context context, AppSettings appSettings) {
        super(context, appSettings);
        this.titleRes = R.string.title_app_auto_start;
        this.iconRes = R.drawable.ic_device_hub_black_24dp;
    }

    @Override
    boolean getSwitchState() {
        return getAppSettings().isStart();
    }

    @Override
    void applySwitchState(boolean checked) {
        super.applySwitchState(checked);
        getAppSettings().setStart(checked);
        XAshmanManager.get()
                .addOrRemoveStartBlockApps(new String[]{getAppSettings().getPkgName()},
                        checked ? XAshmanManager.Op.ADD : XAshmanManager.Op.REMOVE);
    }
}

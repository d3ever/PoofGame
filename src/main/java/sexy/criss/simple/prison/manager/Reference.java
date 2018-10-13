package sexy.criss.simple.prison.manager;

import com.sun.istack.internal.NotNull;
import sexy.criss.simple.prison.utils.Utils;

public enum Reference {
    PERMISSIONS_ERROR("&7Извините, у вас недостаточно прав, чтобы использовать это."),
    NUMBER_FAIL("&7Вы ввели неверное значение."),
    USAGE_SYNTAX("&7Использование: &9/%s&7."),
    TBLOCKS_INTERVAL_UPDATED("&7Интервал регенерации был изменён на новое значение &c%d&7."),
    TBLOCKS_MODE_DEACTIVATED("&7Режим установки блоков был &cдеактивирован&7."),
    TBLOCKS_DONT_ACTIVE("&7Извините, режим установки блоков неактивирован."),
    TBLOCKS_MODE_ACTIVATED("&7Режим установки блоков был &aактивирован&7."),
    TBLOCKS_ALREADY_ACTIVE("&7Режим установки блоков уже активирован.");

    private String message;

    Reference(String message) {
        this.message = message;
    }

    @NotNull
    public String get() {
        return Utils.f(this.message);
    }

    @NotNull
    public String get(@NotNull Object... args) {
        try {
            return Utils.f(this.message, args);
        }catch (Exception ex) {
            return this.message;
        }
    }

}

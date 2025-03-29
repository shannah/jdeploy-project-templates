package {{ packageName }}.services;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LocaleService {

    public final static List<Locale> locales = List.of(Locale.ENGLISH, Locale.FRENCH);

    private static Locale currentLocale;


    public static Locale getLocale() {
        if (currentLocale == null) {
            //default english
            currentLocale = Locale.ENGLISH;

            //if computer locale is supported
            locales.forEach(locale -> {
                if (Objects.equals(locale.getISO3Language(), Locale.getDefault().getISO3Language())) {
                    currentLocale = locale;
                }
            });

            //if settings locale is supported
            UserSettingsService.getLocale().ifPresent(iso3Lang ->
                    locales.forEach(locale -> {
                        if (Objects.equals(locale.getISO3Language(), iso3Lang)) {
                            currentLocale = locale;
                        }
                    }));
        }
        return currentLocale;
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        UserSettingsService.setLocale(currentLocale.getISO3Language());
    }
}

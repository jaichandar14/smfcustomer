// Generated by Dagger (https://dagger.dev).
package com.smf.customer.di.sharedpreference;

import android.content.SharedPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class SharedPreferencesModule_ProvideSharedPreferencesFactory implements Factory<SharedPreferences> {
  private final SharedPreferencesModule module;

  private final Provider<String> mainKeyAliasProvider;

  public SharedPreferencesModule_ProvideSharedPreferencesFactory(SharedPreferencesModule module,
      Provider<String> mainKeyAliasProvider) {
    this.module = module;
    this.mainKeyAliasProvider = mainKeyAliasProvider;
  }

  @Override
  public SharedPreferences get() {
    return provideSharedPreferences(module, mainKeyAliasProvider.get());
  }

  public static SharedPreferencesModule_ProvideSharedPreferencesFactory create(
      SharedPreferencesModule module, Provider<String> mainKeyAliasProvider) {
    return new SharedPreferencesModule_ProvideSharedPreferencesFactory(module, mainKeyAliasProvider);
  }

  public static SharedPreferences provideSharedPreferences(SharedPreferencesModule instance,
      String mainKeyAlias) {
    return Preconditions.checkNotNullFromProvides(instance.provideSharedPreferences(mainKeyAlias));
  }
}
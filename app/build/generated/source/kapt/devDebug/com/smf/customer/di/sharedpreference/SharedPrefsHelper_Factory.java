// Generated by Dagger (https://dagger.dev).
package com.smf.customer.di.sharedpreference;

import android.content.SharedPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class SharedPrefsHelper_Factory implements Factory<SharedPrefsHelper> {
  private final Provider<SharedPreferences> mSharedPreferencesProvider;

  public SharedPrefsHelper_Factory(Provider<SharedPreferences> mSharedPreferencesProvider) {
    this.mSharedPreferencesProvider = mSharedPreferencesProvider;
  }

  @Override
  public SharedPrefsHelper get() {
    return newInstance(mSharedPreferencesProvider.get());
  }

  public static SharedPrefsHelper_Factory create(
      Provider<SharedPreferences> mSharedPreferencesProvider) {
    return new SharedPrefsHelper_Factory(mSharedPreferencesProvider);
  }

  public static SharedPrefsHelper newInstance(SharedPreferences mSharedPreferences) {
    return new SharedPrefsHelper(mSharedPreferences);
  }
}
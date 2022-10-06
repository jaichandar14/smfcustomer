// Generated by Dagger (https://dagger.dev).
package com.smf.customer.di.retrofit;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class NetworkModule_ProvidesOkHttpClientFactory implements Factory<OkHttpClient> {
  private final NetworkModule module;

  private final Provider<HttpLoggingInterceptor> httpLoggingInterceptorProvider;

  public NetworkModule_ProvidesOkHttpClientFactory(NetworkModule module,
      Provider<HttpLoggingInterceptor> httpLoggingInterceptorProvider) {
    this.module = module;
    this.httpLoggingInterceptorProvider = httpLoggingInterceptorProvider;
  }

  @Override
  public OkHttpClient get() {
    return providesOkHttpClient(module, httpLoggingInterceptorProvider.get());
  }

  public static NetworkModule_ProvidesOkHttpClientFactory create(NetworkModule module,
      Provider<HttpLoggingInterceptor> httpLoggingInterceptorProvider) {
    return new NetworkModule_ProvidesOkHttpClientFactory(module, httpLoggingInterceptorProvider);
  }

  public static OkHttpClient providesOkHttpClient(NetworkModule instance,
      HttpLoggingInterceptor httpLoggingInterceptor) {
    return Preconditions.checkNotNullFromProvides(instance.providesOkHttpClient(httpLoggingInterceptor));
  }
}

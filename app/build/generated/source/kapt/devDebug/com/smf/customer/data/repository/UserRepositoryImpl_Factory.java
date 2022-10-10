// Generated by Dagger (https://dagger.dev).
package com.smf.customer.data.repository;

import com.smf.customer.data.api_service.UserService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class UserRepositoryImpl_Factory implements Factory<UserRepositoryImpl> {
  private final Provider<UserService> userServiceProvider;

  public UserRepositoryImpl_Factory(Provider<UserService> userServiceProvider) {
    this.userServiceProvider = userServiceProvider;
  }

  @Override
  public UserRepositoryImpl get() {
    return newInstance(userServiceProvider.get());
  }

  public static UserRepositoryImpl_Factory create(Provider<UserService> userServiceProvider) {
    return new UserRepositoryImpl_Factory(userServiceProvider);
  }

  public static UserRepositoryImpl newInstance(UserService userService) {
    return new UserRepositoryImpl(userService);
  }
}
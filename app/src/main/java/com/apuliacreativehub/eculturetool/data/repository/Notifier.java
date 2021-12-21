package com.apuliacreativehub.eculturetool.data.repository;

import java.io.IOException;

import retrofit2.Response;

public interface Notifier {
    <T> void notifyResult(final Response<T> response, final RepositoryCallback<T> callback);

    <T> void notifyError(final IOException ioe, RepositoryCallback<T> callback);
}

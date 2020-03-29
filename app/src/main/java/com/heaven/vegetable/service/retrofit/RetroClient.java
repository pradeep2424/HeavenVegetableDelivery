package com.heaven.vegetable.service.retrofit;

import android.content.Context;

import com.heaven.vegetable.utils.ConstantValues;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pradeep Jadhav.
 */

public class RetroClient {

    private static Retrofit getRetrofitInstance(Context mContext) {

        return new Retrofit.Builder()
                .baseUrl(ConstantValues.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getRequestHeader(mContext))
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .client(okClient)
                .build();
    }

//        certificate
//    public static OkHttpClient getRequestHeader(Context mContext) {
//        InputStream cert = null;
//        try {
//            // loading CAs from an InputStream
////            CertificateFactory cf = CertificateFactory.getInstance("X.509");
////            cert = mContext.getResources().openRawResource(R.raw.sogo_live);
//
//            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
//            cert = mContext.getResources().openRawResource(R.raw.sogo_qauc);
////            cert = mContext.getResources().openRawResource(R.raw.sogo_live);
//            Certificate ca;
//
//            ca = cf.generateCertificate(cert);
//
//            // creating a KeyStore containing our trusted CAs
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//            // creating a TrustManager that trusts the CAs in our KeyStore
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//            // creating an SSLSocketFactory that uses our TrustManager
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, tmf.getTrustManagers(), null);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                cert.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .readTimeout(180, TimeUnit.SECONDS)
//                .connectTimeout(180, TimeUnit.SECONDS)
//                .addInterceptor(
//                        new Interceptor() {
//                            @Override
//                            public Response intercept(Interceptor.Chain chain) throws IOException {
//                                Request original = chain.request();
//
//                                // Request customization: add request headers
//                                Request.Builder requestBuilder = original.newBuilder()
//                                        .addHeader("User-Agent", "SoGoSurvey")
//                                        .method(original.method(), original.body());
//                                Request request = requestBuilder.build();
//                                return chain.proceed(request);
//                            }
//                        })
//                .build();
//
//        return okHttpClient;
//    }

//    local use
    public static OkHttpClient getRequestHeader(Context mContext) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
//                .addInterceptor(
//                        new Interceptor() {
//                            @Override
//                            public Response intercept(Interceptor.Chain chain) throws IOException {
//                                Request original = chain.request();
//
//                                // Request customization: add request headers
//                                Request.Builder requestBuilder = original.newBuilder()
//                                        .addHeader("User-Agent", "SoGoSurvey")
//                                        .method(original.method(), original.body());
//                                Request request = requestBuilder.build();
//                                return chain.proceed(request);
//                            }
//                        })
                .build();

        return okHttpClient;
    }

    public static ApiInterface getApiService(Context mContext) {
        return getRetrofitInstance(mContext).create(ApiInterface.class);
    }
}

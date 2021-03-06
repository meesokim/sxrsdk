/* Copyright 2016 Samsung Electronics Co., LTD
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.samsungxr.asynchronous;

import java.io.IOException;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRCompressedImage;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRAndroidResource.CancelableCallback;
import com.samsungxr.asynchronous.Throttler.AsyncLoader;
import com.samsungxr.asynchronous.Throttler.AsyncLoaderFactory;
import com.samsungxr.asynchronous.Throttler.GlConverter;

import com.samsungxr.utility.Log;

/**
 * Async resource loading: compressed textures.
 * 
 */

class AsyncCompressedTexture {

    /*
     * The API
     */
    static void loadTexture(SXRContext gvrContext,
            CancelableCallback<SXRCompressedImage> callback,
            SXRAndroidResource resource, int priority) {
        AsyncManager.get().getScheduler().registerCallback(gvrContext,
                TEXTURE_CLASS, callback, resource, priority);
    }

    /*
     * Singleton
     */

    private static final Class<SXRCompressedImage> TEXTURE_CLASS = SXRCompressedImage.class;
    
    private static AsyncCompressedTexture sInstance = new AsyncCompressedTexture();

    /**
     * Gets the {@link AsynCompressedTexture} singleton for loading compressed textures.
     * 
     * @return The {@link AsynCompressedTexture} singleton.
     */
    public static AsyncCompressedTexture get() {
        return sInstance;
    }

    private AsyncCompressedTexture() {
        AsyncManager.get().registerDatatype(TEXTURE_CLASS,
                new AsyncLoaderFactory<SXRCompressedImage, CompressedTexture>() {
                    @Override
                    AsyncLoader<SXRCompressedImage, CompressedTexture> threadProc(
                            SXRContext gvrContext, SXRAndroidResource request,
                            CancelableCallback<SXRCompressedImage> callback,
                            int priority) {
                        return new AsyncLoadTextureResource(gvrContext, request,
                                callback, priority);
                    }
                });
    }

    /*
     * Asynchronous loader
     */

    private static class AsyncLoadTextureResource
            extends AsyncLoader<SXRCompressedImage, CompressedTexture> {

        private static final GlConverter<SXRCompressedImage, CompressedTexture> sConverter = new GlConverter<SXRCompressedImage, CompressedTexture>() {

            @Override
            public SXRCompressedImage convert(SXRContext gvrContext,
                                              CompressedTexture compressedTexture) {
                return compressedTexture == null ? null
                        : compressedTexture.toTexture(gvrContext,
                                                      SXRCompressedImage.BALANCED);
            }
        };

        protected AsyncLoadTextureResource(SXRContext gvrContext,
                SXRAndroidResource request,
                CancelableCallback<SXRCompressedImage> callback,
                int priority) {
            super(gvrContext, sConverter, request, callback);
        }

        @Override
        protected CompressedTexture loadResource() {
            SXRCompressedTextureLoader loader = resource.getCompressedLoader();
            CompressedTexture compressedTexture = null;
            try {
                compressedTexture = CompressedTexture
                        .parse(resource.getStream(), false, loader);
                Log.d("ASYNC", "parse compressed texture %s", resource);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                resource.closeStream();
            }
            return compressedTexture;
        }
    }
}

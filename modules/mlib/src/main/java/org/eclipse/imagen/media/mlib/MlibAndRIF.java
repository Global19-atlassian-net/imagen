/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.eclipse.imagen.media.mlib;
import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import org.eclipse.imagen.ImageLayout;
import java.util.Map;
import org.eclipse.imagen.media.opimage.RIFUtil;

/**
 * A <code>RIF</code> supporting the "And" operation in the
 * rendered image mode using MediaLib.
 *
 * @see org.eclipse.imagen.operator.AndDescriptor
 * @see MlibAndOpImage
 *
 */
public class MlibAndRIF implements RenderedImageFactory { // AndRIF {

    /** Constructor. */
    public MlibAndRIF() {}

    /**
     * Creates a new instance of <code>MlibAndOpImage</code> in
     * the rendered image mode.
     *
     * @param args  The source images.
     * @param hints  May contain rendering hints and destination image layout.
     */
    public RenderedImage create(ParameterBlock args,
                                RenderingHints hints) {
        ImageLayout layout = RIFUtil.getImageLayoutHint(hints);
        

        if (!MediaLibAccessor.isMediaLibCompatible(args, layout) ||
            !MediaLibAccessor.hasSameNumBands(args, layout)) {
            return null;
        }

        /* Check whether dest has data type of float or double. */
        if (layout != null) {
            SampleModel sm = layout.getSampleModel(null);

            if (sm != null) {
                int dtype = sm.getDataType();
                if (dtype == DataBuffer.TYPE_FLOAT ||
                    dtype == DataBuffer.TYPE_DOUBLE) {
                    return null;
                }
            }
        }

	return new MlibAndOpImage(args.getRenderedSource(0),
                                  args.getRenderedSource(1),
                                  hints, layout);
    }
}

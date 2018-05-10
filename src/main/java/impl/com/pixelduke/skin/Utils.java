/*
 * Copyright (c) 2018 Pixel Duke (Pedro Duque Vieira - www.pixelduke.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *   * Neither the name of Pixel Duke, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL PIXEL DUKE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package impl.com.pixelduke.skin;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

class Utils {
    // Utility method to change a Node size by filling a bounding box with width = targetWidth and height = targetHeight
    // while ratio is still preserved.
    static void changeImageSize(Node node, double targetWidth, double targetHeight) {
        double nodeHeight = node.getBoundsInLocal().getHeight();
        double nodeWidth = node.getBoundsInLocal().getWidth();
        double newHeightPercentage = targetHeight / nodeHeight;
        double newWidthPercentage = targetWidth / nodeWidth;

        double newSizePercentage;
        if (newWidthPercentage < newHeightPercentage) {
            // We can change the width by newHeightPercentage and reach the targetWidth
            newSizePercentage = newHeightPercentage;
        } else {
            // We can change the height by NewWidthPercentage and reach the targetHeight
            newSizePercentage = newWidthPercentage;
        }
        if (node instanceof ImageView) {
            ImageView imageView = (ImageView) node;
            imageView.setFitHeight(nodeHeight * newSizePercentage);
            imageView.setFitWidth(nodeWidth * newSizePercentage);
        } else {
            node.resize(nodeWidth * newSizePercentage, nodeHeight * newSizePercentage);
        }
    }
}

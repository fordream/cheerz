/* Copyright (c) 2011-2013 Tang Ke
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
package z.lib.com.aretha.slidemenu.utils;

import z.lib.com.aretha.slidemenu.utils.ScrollDetectors.ScrollDetector;
import android.view.View;

public interface ScrollDetectorFactory {
	/**
	 * Create new instance of {@link ScrollDetector} based on the parameter v
	 * 
	 * @param v
	 * @return
	 */
	public ScrollDetector newScrollDetector(View v);
}

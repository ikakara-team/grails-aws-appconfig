/* Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ikakara.appconfig.util

import java.text.DecimalFormat

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j("LOG")
class Stats {
  static final int KEEP_RATIO = 2

  long start = System.currentTimeMillis()
  long total_time = 0
  int events = 0
  String name

  Stats(String name) {
    this.name = name
  }

  synchronized void add(long duration) {
    total_time += duration
    events++
  }

  synchronized Info get() {
    long end = System.currentTimeMillis()
    double dt = (end - start) / 1000.0
    Info ret
    if (events == 0 || dt == 0.0) {
      ret = new Info(0.0, 0.0)
    } else {
      ret = new Info(total_time / (double) events, events / dt)
    }
    start += (long)((end - start) / KEEP_RATIO)
    events /= KEEP_RATIO
    total_time /= KEEP_RATIO
    return ret
  }

  class Info {
    double ave_time
    double events_per_sec

    Info(double ave_time, double events_per_sec) {
      this.ave_time = ave_time
      this.events_per_sec = events_per_sec
    }

    @Override
    String toString() {
      final DecimalFormat df = new DecimalFormat("0.000")
      return "Info [name=" + name + " ave_time=" + df.format(ave_time) + ", events_per_sec=" + df.format(events_per_sec) + "]"
    }
  }
}

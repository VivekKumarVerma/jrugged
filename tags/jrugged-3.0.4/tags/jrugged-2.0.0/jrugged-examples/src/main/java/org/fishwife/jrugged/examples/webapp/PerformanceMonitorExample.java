/* Copyright 2009-2010 Comcast Interactive Media, LLC.
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
package org.fishwife.jrugged.examples.webapp;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fishwife.jrugged.PerformanceMonitor;
import org.fishwife.jrugged.aspects.PerformanceMonitorAspect;
import org.fishwife.jrugged.examples.ResponseTweaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller()
public class PerformanceMonitorExample {

    @Autowired
    private PerformanceMonitorAspect performanceAspect;
    public PerformanceMonitorAspect getPerformanceAspect() {
        return performanceAspect;
    }
    public void setPerformanceAspect(PerformanceMonitorAspect performanceAspect) {
        this.performanceAspect = performanceAspect;
    }

    @Autowired
    private ResponseTweaker responseTweaker;
    public ResponseTweaker getResponseTweaker() {
        return responseTweaker;
    }
    public void setResponseTweaker(ResponseTweaker responseTweaker) {
        this.responseTweaker = responseTweaker;
    }

    @RequestMapping("/performanceMonitor")
    public ModelAndView viewMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int delayedFor = responseTweaker.delay();
        ModelAndView view = new ModelAndView("perf-monitor");
        view.addObject("delay", new Integer(delayedFor));
        return view;
    }

    @RequestMapping("/performanceMonitor/stats")
    public ModelAndView viewPerformanceMonitor(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final StringBuilder sb = new StringBuilder();
        for (String monitorName : performanceAspect.getMonitors().keySet()) {
            PerformanceMonitor m = performanceAspect.getMonitor(monitorName);
            sb.append(String.format("[%s]", monitorName)).append("\n");
            // Go through all methods and invoke those with ManagedAttribute
            // marker annotations
            Method[] methods = m.getClass().getMethods();
            for (Method monitorMethod : methods) {
                if (monitorMethod.getName().startsWith("get")) {
                    sb.append(
                        String.format("\t%s: %s\n",
                            monitorMethod.getName().substring(3),
                            monitorMethod.invoke(m, new Object[] {})
                        )
                    );
                }
            }
            sb.append("\n");
        }
        response.setContentType("text/plain");
        response.getWriter().println(sb.toString());
        return null;
    }    
}

/**
 * Copyright 2015, Red Hat, Inc.
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

package org.jbpm.examples.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.examples.util.ProcessLocal;
import org.jbpm.examples.util.StartupBean;

public class ProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@EJB
	private ProcessLocal processService;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String processName = req.getParameter("processName");
		long processInstanceId = -1;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			processInstanceId = processService
					.startProcess("");
			System.out.println("Process instance " + StartupBean.DEPLOYMENT_ID
					+ "---" + processInstanceId
					+ " has been successfully started.");
		} catch (Exception e) {
			throw new ServletException(e);
		}

		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");

		// create HTML response
		PrintWriter writer = res.getWriter();
		writer.append("<!DOCTYPE html>\r\n").append("<html>\r\n")
				.append("		<head>\r\n")
				.append("			<title>Welcome message</title>\r\n")
				.append("		</head>\r\n").append("		<body>\r\n");
		writer.append("	Process ID is " + processInstanceId + "!\r\n");
		writer.append("		</body>\r\n").append("</html>\r\n");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
}
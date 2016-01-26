<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Footer for home page
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>

<%
    String sidebar = (String) request.getAttribute("dspace.layout.sidebar");
%>

            <%-- Right-hand side bar if appropriate --%>
<%
    if (sidebar != null)
    {
%>
	</div>
	<div class="col-md-3">
                    <%= sidebar %>
    </div>
    </div>       
<%
    }
%>
</div>
</main>
            <%-- Page footer --%>
             <footer class="navbar navbar-bottom">
             <div id="designedby" class="container text-muted">
             <!--<fmt:message key="jsp.layout.footer-default.theme-by"/> <a href="http://www.cineca.it"><img
                                    src="<%= request.getContextPath() %>/image/logo-cineca-small.png"
                                    alt="Logo CINECA" /></a>-->
			<div id="footer_feedback" class="pull-right">                                    
                                <!--<p class="text-muted"><fmt:message key="jsp.layout.footer-default.text"/>&nbsp;-
                                <a target="_blank" href="<%= request.getContextPath() %>/feedback"><fmt:message key="jsp.layout.footer-default.feedback"/></a>
                                <a href="<%= request.getContextPath() %>/htmlmap"></a></p>-->
                                </div>
			</div>
			<!-- rcaap footer -->
			<table class="pageFooterBar" style="background-color:white;">
				<tbody>
					<tr style="font: 10px/10px arial, helvetica, sans-serif; color: #666;">
								<td colspan="2" rowspan="1" style="text-align:left;padding-left: 10px;">Promotores do RCAAP</td>
								<td width="30">&nbsp;</td>
								<td colspan="3" rowspan="1" style="text-align:left;">Financiadores do RCAAP</td>
		</tr>
					<tr style="font: 10px/10px serif;">
						<td colspan="8" rowspan="1"><hr style="height: 1px; border: 0; color: #ccc; background-color: #ccc;" /></td>
					</tr>
					<tr valign="bottom">
							<td style="padding-left: 10px;padding-right: 10px;">
								<a href="http://www.fct.pt" target="_blank" title="Funda&ccedil;&atilde;o para a Ci&ecirc;ncia e a Tecnologia">
									<img src="/image/logos/fctfccn.gif" alt="Funda&ccedil;&atilde;o para a Ci&ecirc;ncia e a Tecnologia" border="0" />
								</a>
							</td>
							<td style="padding-right: 10px;">
								<a href="http://www.uminho.pt" target="_blank" title="Universidade do Minho">
									<img src="/image/logos/uminho.gif" alt="Universidade do Minho" border="0" />
								</a>
							</td>
							<td width="50%">&nbsp;</td>
							<td style="padding-right: 10px;">
								<a href="http://www.portugal.gov.pt" target="_blank" title="Governo Portugu&ecirc;s">
									<img src="/image/logos/govpt.gif" alt="Governo Portugu&ecirc;s" border="0" />
								</a>
							</td>
							<td style="padding-right: 10px;">
								<a href="http://www.portugal.gov.pt/pt/os-ministerios/ministerio-da-educacao-e-ciencia.aspx" target="_blank" title="Minist&eacute;rio da Educa&ccedil;&atilde;o e Ci&ecirc;ncia">
									<img src="/image/logos/mec.gif" alt="Minist&eacute;rio da Educa&ccedil;&atilde;o e Ci&ecirc;ncia" border="0" />
								</a>
							</td>
							<td style="padding-right: 10px;">
								<a href="http://www.qca.pt/pos/posc.asp" target="_blank" title="PO Sociedade do Conhecimento (POSC)">
									<img src="/image/logos/pos.gif" alt="PO Sociedade do Conhecimento (POSC)" border="0" />
								</a>
							</td>
							<td style="padding-right: 10px;">
								<a href="http://europa.eu" target="_blank" title="Portal oficial da Uni&atilde;o Europeia">
									<img src="/image/logos/ue.gif" alt="Portal oficial da Uni&atilde;o Europeia" border="0" />
								</a>
							</td>
							<td>&nbsp;</td>
					</tr>
				</tbody>
</table>
    </footer>
    </body>
</html>
[![Build Status](https://testes.jenkins.rcaap.pt/buildStatus/icon?job=SARI - Public)](https://testes.jenkins.rcaap.pt/job/SARI%20-%20Public/)

To add piwik (statistics access for openaire)

In the footer add the following code:

      <%@ page import="org.dspace.core.ConfigurationManager" %>
      <%@ page import="org.dspace.content.Item" %>

      <%

      String piwikID = ConfigurationManager.getProperty("piwik.id");
      String baseUrl = ConfigurationManager.getProperty("dspace.hostname");
      String handle;


      try{
      Item item = (Item) request.getAttribute("item");

      // get the handle if the item has one yet
      handle = item.getHandle();
      }
      catch(Exception e){
      	handle = null;
      	//e.printstacktrace();
      }

      %>

      <!-- Piwik -->
      <script type="text/javascript">
        var _paq = _paq || [];
        _paq.push(['trackPageView']);
        _paq.push(['enableLinkTracking']);
        (function() {
          var u="//analytics.openaire.eu/";
          _paq.push(['setTrackerUrl', u+'piwik.php']);
          _paq.push(['setSiteId', <%=piwikID%>]);
          <% if(handle != null){%>
          	_paq.push(['setCustomVariable', 1, 'oaipmhID',"oai:<%= baseUrl %>/<%=handle %>", 'page']);
          <%}%>
          var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
          g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
        })();
      </script>
      <noscript><p><img src="//analytics.openaire.eu/piwik.php?idsite=<%=piwikID%>" style="border:0;" alt="" /></p></noscript>


And in the build.properties add the piwik.id of the institution.

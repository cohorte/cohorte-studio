package org.cohorte.studio.eclipse.ui.node.project;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;

import org.cohorte.studio.eclipse.api.annotations.NonNull;
import org.cohorte.studio.eclipse.api.objects.IHttpTransport;
import org.cohorte.studio.eclipse.api.objects.INode;
import org.cohorte.studio.eclipse.api.objects.IRuntime;
import org.cohorte.studio.eclipse.api.objects.ITransport;
import org.cohorte.studio.eclipse.api.objects.IXmppTransport;
import org.cohorte.studio.eclipse.core.api.IProjectContentManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Creatable;

/**
 * Node project content manager.
 * 
 * @author Ahmad Shahwan
 *
 */
@Creatable
public class CNodeContentManager implements IProjectContentManager<INode> {
	
	private static final String CONF = "conf"; //$NON-NLS-1$
	private static final String RUN_JS = new StringBuilder().append(CONF).append("/run.js").toString(); //$NON-NLS-1$
	
	@SuppressWarnings("nls")
	private interface IJsonKeys {
		String NODE = "node";
		String SHELL_PORT = "shell-port";
		String HTTP_PORT = "http-port";
		String NAME = "name";
		String TOP_COMPOSER = "top-composer";
		String CONSOLE = "console";
		String COHORTE_VERSION = "cohorte-version";
		String TRANSPORT = "transport";
		String TRANSPORT_HTTP = "transport-http";
		String HTTP_IPV = "http-ipv";
		String TRANSPORT_XMPP = "transport-xmpp";
		String XMPP_SERVER = "xmpp-server";
		String XMPP_USER_ID = "xmpp-user-jid";
        String XMPP_USER_PASSWORD = "xmpp-user-password";
        String XMPP_PORT = "xmpp-port";
	}
	
	/**
	 * Constructor.
	 */
	@Inject
	public CNodeContentManager() {
	}

	@Override
	public void populate(@NonNull IProject aProject, INode aModel) throws CoreException {
		if (!aProject.isOpen()) {
			aProject.open(null);
		}
		aProject.getFolder(CONF).create(true, true, null);
		IFile wRun = aProject.getFile(RUN_JS);
		StringWriter wBuffer = new StringWriter();
		
		Map<String, Object> wProperties = new HashMap<>(1);
		wProperties.put(JsonGenerator.PRETTY_PRINTING, true);
		
		JsonWriter wJWriter = Json.createWriterFactory(wProperties).createWriter(wBuffer);
		
		JsonBuilderFactory wJ = Json.createBuilderFactory(null);
		JsonObjectBuilder wJson = wJ.createObjectBuilder()
			.add(IJsonKeys.NODE, wJ.createObjectBuilder()
				.add(IJsonKeys.SHELL_PORT, 0)
				.add(IJsonKeys.HTTP_PORT, 0)
				.add(IJsonKeys.NAME, aModel.getName())
				.add(IJsonKeys.TOP_COMPOSER, aModel.isComposer())
				.add(IJsonKeys.CONSOLE, true));
		JsonArrayBuilder wTransports = wJ.createArrayBuilder();
		for (ITransport wTransport : aModel.getTransports()) {
			wTransports.add(wTransport.getName());
			if (wTransport instanceof IHttpTransport) {
				IHttpTransport wHttp = (IHttpTransport) wTransport;
				String wVer = wHttp.getVersion() == IHttpTransport.EVersion.IPV4 ? "4" : "6"; //$NON-NLS-1$//$NON-NLS-2$
				wJson.add(IJsonKeys.TRANSPORT_HTTP, wJ.createObjectBuilder().add(IJsonKeys.HTTP_IPV, wVer));  
			}
			if (wTransport instanceof IXmppTransport) {
				IXmppTransport wXmpp = (IXmppTransport) wTransport;
				JsonObjectBuilder wJsonXmpp = wJ.createObjectBuilder()
					.add(IJsonKeys.XMPP_SERVER, wXmpp.getHostname())
					.add(IJsonKeys.XMPP_PORT, wXmpp.getPort());
				if (wXmpp.getUsername() != null) {
					wJsonXmpp
						.add(IJsonKeys.XMPP_USER_ID, wXmpp.getUsername())
						.add(IJsonKeys.XMPP_USER_PASSWORD, wXmpp.getPassword());
				}
				wJson
					.add(IJsonKeys.TRANSPORT_XMPP, wJsonXmpp);
			}
		}
		wJson.add(IJsonKeys.TRANSPORT, wTransports);
		IRuntime wRuntime = aModel.getRuntime();
		if (wRuntime != null) {
			wJson.add(IJsonKeys.COHORTE_VERSION, wRuntime.getVersion());
		}
		JsonObject wRunJs = wJson.build();
		
		wJWriter.write(wRunJs);
		
		InputStream wInStream = new ByteArrayInputStream(wBuffer.toString().getBytes());
		wRun.create(wInStream, true, null);
	}
}

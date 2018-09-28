import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class RemoteHandler  {

	public static void main(String[] args) {
          //Add Your Test Here
	}	
	public static int remoteExec(String userName , String remoteMachineAddr ,int remotePort , String remotePassword , String remoteCommand) throws JSchException, IOException {
	  JSch js = new JSch();
	  Session s = js.getSession(userName,remoteMachineAddr,remotePort);
	  s.setPassword(remotePassword);
	  Properties config = new Properties();
	  config.put("StrictHostKeyChecking", "no");
	  s.setConfig(config);
	  s.connect();

	  Channel c = s.openChannel("exec");
	  ChannelExec ce = (ChannelExec) c;

	  ce.setCommand(remoteCommand);
	  ce.setErrStream(System.err);

	  ce.connect();

	  BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
	  String line;
	  while ((line = reader.readLine()) != null) {
	    System.out.println(line);
	  }

	  ce.disconnect();
	  s.disconnect();

	  System.out.println("Exit code: " + ce.getExitStatus());
          return ce.getExitStatus();
	}

	public static String remoteCmd(String userName , String remoteMachineAddr ,int remotePort , String remotePassword , String remoteCommand) throws JSchException, IOException {
	  JSch js = new JSch();
	  Session s = js.getSession(userName,remoteMachineAddr,remotePort);
	  s.setPassword(remotePassword);
	  Properties config = new Properties();
	  config.put("StrictHostKeyChecking", "no");
	  s.setConfig(config);
	  s.connect();

	  Channel c = s.openChannel("exec");
	  ChannelExec ce = (ChannelExec) c;

	  ce.setCommand(remoteCommand);
	  ce.setErrStream(System.err);

	  ce.connect();

	  BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
          String resultLine = "";
	  String line;
	  while ((line = reader.readLine()) != null) {
	    //System.out.println(line);
            resultLine = resultLine + line;
	  }
	  ce.disconnect();
	  s.disconnect();
	  System.out.println("Exit code: " + ce.getExitStatus());
          return resultLine;
	}



	public static void  remoteMkdir(String userName , String remoteMachineAddr ,int remotePort , String remotePassword , String remoteDir) throws JSchException, IOException {
	  JSch js = new JSch();
	  Session s = js.getSession(userName,remoteMachineAddr,remotePort);
	  s.setPassword(remotePassword);
	  Properties config = new Properties();
	  config.put("StrictHostKeyChecking", "no");
	  s.setConfig(config);
	  s.connect();

	  Channel c = s.openChannel("exec");
	  ChannelExec ce = (ChannelExec) c;

	  ce.setCommand("mkdir "+remoteDir);
	  ce.setErrStream(System.err);

	  ce.connect();

	  BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
	  String line;
	  while ((line = reader.readLine()) != null) {
	    System.out.println(line);
	  }

	  ce.disconnect();
	  s.disconnect();

	  System.out.println("Exit code: " + ce.getExitStatus());

	}

	public static void  remoteCopy(String userName , String remoteMachineAddr ,int remotePort , String remotePassword , String srcFileName ,String destFileName) throws JSchException, IOException, SftpException {
	  JSch js = new JSch();
	  Session s = js.getSession(userName, remoteMachineAddr, remotePort);
	  s.setPassword(remotePassword);
	  Properties config = new Properties();
	  config.put("StrictHostKeyChecking", "no");
	  s.setConfig(config);
	  s.connect();

	  Channel c = s.openChannel("sftp");
	  ChannelSftp ce = (ChannelSftp) c;

	  ce.connect();

	  ce.put(srcFileName,destFileName);

	  ce.disconnect();
	  s.disconnect();    
	}

}




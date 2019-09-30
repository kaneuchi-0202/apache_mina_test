package apache_mina_test.any_response.scp;

import java.io.IOException;

import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;

/**
 * ログインシェルでログインし、コマンド実行可能なSSHサーバ。
 */
public class ScpServerMain {

	public static void main(String[] args) {

		int port = 22;

		SshServer sshd = SshServer.setUpDefaultServer();

		sshd.setPort(port);
		sshd.setPasswordAuthenticator(new MyPasswordAuthenticator());
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		sshd.setCommandFactory(new MyCommandFactory());

		try {
			sshd.start();
			System.out.println("sshサーバ起動。ポート：" + port);

			// sshd.start()だけではプログラムが終了してしまうため無限ループ
			while (true)
				;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				sshd.stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * パスワード認証用クラス。
	 */
	static class MyPasswordAuthenticator implements PasswordAuthenticator {

		@Override
		public boolean authenticate(String username, String password, ServerSession session)
				throws PasswordChangeRequiredException, AsyncAuthException {

			// ユーザID：user、パスワード：passwordのみ認証成功とする
			return "user".equals(username) && "password".equals(password);
		}
	}
}

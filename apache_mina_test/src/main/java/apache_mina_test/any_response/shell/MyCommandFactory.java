package apache_mina_test.any_response.shell;

import java.io.IOException;

import org.apache.sshd.common.util.threads.CloseableExecutorService;
import org.apache.sshd.common.util.threads.ThreadUtils;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.AbstractCommandSupport;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;

/**
 * 自作のレスポンスを返却させるための、CommandFactoryクラス。
 */
public class MyCommandFactory implements CommandFactory {

	@Override
	public Command createCommand(ChannelSession channel, String command) throws IOException {

		System.out.println("受信コマンド：" + command);

		// 受信コマンドに応じたCommandクラスを返却する
		if (command.equals("hoge")) {
			return new CommandA(command, ThreadUtils.newCachedThreadPool("hoge-thread"));
		} else {
			return new CommandB(command, ThreadUtils.newCachedThreadPool("fuga-thread"));
		}
	}

	/**
	 * コマンドAクラス。
	 */
	class CommandA extends AbstractCommandSupport {

		protected CommandA(String command, CloseableExecutorService executorService) {
			super(command, executorService);
		}

		@Override
		public void run() {
			try {
				// コマンドA用のレスポンスを返却
				this.out.write("hoge\r\n".getBytes());
				this.out.flush();

				// リザルトコード0で処理を終了
				this.onExit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * コマンドAクラス。
	 */
	class CommandB extends AbstractCommandSupport {

		protected CommandB(String command, CloseableExecutorService executorService) {
			super(command, executorService);
		}

		@Override
		public void run() {
			try {
				// コマンドB用のレスポンスを返却
				this.out.write("fuga\r\n".getBytes());
				this.out.write("fuga\r\n".getBytes());
				this.out.flush();

				// リザルトコード0で処理を終了
				this.onExit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

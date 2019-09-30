package apache_mina_test.any_response.scp;

import java.io.IOException;

import org.apache.sshd.common.util.threads.CloseableExecutorService;
import org.apache.sshd.server.command.AbstractCommandSupport;

public class SshCommand extends AbstractCommandSupport{

	protected SshCommand(String command, CloseableExecutorService executorService) {
		super(command, executorService);
	}

	@Override
	public void run() {
		try {
			while (true) {
				// 無限ループで受信を待機
				byte[] buff = new byte[1024];
				if (this.in.read(buff) == -1) {

					// 終端に達した場合は終了
					System.out.println("EOF");
					break;
				}
				String recv = new String(buff);
				System.out.println("受信文字列：" + recv);

				// 受信文字列に応じたレスポンスを返却
				String msg = "[response]";
				if (recv.contains("hoge")) {
					msg += "hoge\r\n";
				} else if(recv.contains("fuga")){
					msg += "fuga\r\nfuga\r\n";
				} else if (recv.contains("quit")) {
					break;
				} else {
					msg += "unknown command receive.\r\n";
				}
				this.out.write(msg.getBytes());
				this.out.flush();
			}

			// リザルトコード0で処理を終了
			this.onExit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

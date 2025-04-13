import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.RemoteNumberGenerateServiceGrpc;
import ru.otus.protobuf.ResponseMessage;
import ru.otus.protobuf.StartMessage;

@SuppressWarnings({"squid:S106", "squid:S2142"})
public class GRPCClient {
    private static final Logger log = LoggerFactory.getLogger(GRPCServer.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int FIRST_VALUE = 0;
    private static final int LAST_VALUE = 30;
    private static final int MAX_CLIENT_ITERATION = 50;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        try {
            var newStub = RemoteNumberGenerateServiceGrpc.newStub(channel);
            StartMessage startMessage = StartMessage.newBuilder()
                    .setFirstValue(FIRST_VALUE)
                    .setLastValue(LAST_VALUE)
                    .build();
            Queue<Long> dataBuffer = new ConcurrentLinkedQueue<>();
            newStub.generateNumber(startMessage, new StreamObserver<ResponseMessage>() {
                @Override
                public void onNext(ResponseMessage responseMessage) {
                    long value = responseMessage.getValue();
                    log.info("New value from server: {}", value);
                    dataBuffer.offer(value);
                }

                @Override
                public void onError(Throwable t) {
                    log.error(t.getMessage());
                }

                @Override
                public void onCompleted() {
                    log.info("Completed!");
                }
            });

            long currentValue = 0;
            for (int i = 0; i <= MAX_CLIENT_ITERATION; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long lastValueFromServer = 0;
                Long valueFromServer;
                while ((valueFromServer = dataBuffer.poll()) != null) {
                    lastValueFromServer = valueFromServer;
                }
                currentValue = currentValue + lastValueFromServer + 1;
                log.info("Current value: {}", currentValue);
            }
            
        } finally {
            channel.shutdown();
        }
    }
}

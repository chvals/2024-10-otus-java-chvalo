import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.atomic.AtomicLong;
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
        AtomicLong currentValueFromServer = new AtomicLong(0);
        try {
            var newStub = RemoteNumberGenerateServiceGrpc.newStub(channel);
            StartMessage startMessage = StartMessage.newBuilder()
                    .setFirstValue(FIRST_VALUE)
                    .setLastValue(LAST_VALUE)
                    .build();

            newStub.generateNumber(startMessage, new StreamObserver<ResponseMessage>() {
                @Override
                public void onNext(ResponseMessage responseMessage) {
                    currentValueFromServer.set(responseMessage.getValue());
                    log.info("New value from server: {}", currentValueFromServer);
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
                long lastValueFromServer = currentValueFromServer.getAndSet(0);
                currentValue = currentValue + lastValueFromServer + 1;
                log.info("Current value: {}", currentValue);
            }

        } finally {
            channel.shutdown();
        }
    }
}

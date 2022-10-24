import java.util.*;
import java.util.concurrent.TimeUnit;

public class DoublyLinkedListNode<K, V> {
    // Node of DoublyLinkedList
    DoublyLinkedListNode<K, V>  prev;
    DoublyLinkedListNode<K, V>  next;
    Date dateOfExpiration;
    K key;
    V value;
    private TimeUnit timeUnit;
    private int expireTime;

    public DoublyLinkedListNode(K key, V value, TimeUnit timeUnit, int expireTime) {
        this.key = key;
        this.value = value;
        this.timeUnit = timeUnit;
        this.expireTime = expireTime;
        dateOfExpiration = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOfExpiration);


        switch (this.timeUnit) {
            case MILLISECONDS:
                cal.add(Calendar.MILLISECOND, this.expireTime);
                break;
            case SECONDS:
                cal.add(Calendar.SECOND, this.expireTime);
                break;
            case MINUTES:
                cal.add(Calendar.MINUTE, this.expireTime);
                break;
            case HOURS:
                cal.add(Calendar.HOUR, this.expireTime);
                break;
            default:
                break;
        }

        dateOfExpiration = cal.getTime();
    }

    // check whether an object is expired or not
    public boolean isExpired() {
        if (dateOfExpiration != null) {
            if (dateOfExpiration.before(new Date())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}


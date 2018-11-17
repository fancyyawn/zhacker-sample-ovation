//   Copyright 2012,2013 Vaughn Vernon
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package top.zhacker.ddd.identity.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.zhacker.boot.event.notification.NotificationLog;
import top.zhacker.boot.event.notification.NotificationLogFactory;
import top.zhacker.boot.event.notification.NotificationLogId;
import top.zhacker.boot.event.store.EventStore;


@Service
public class NotificationApplicationService {

    @Autowired
    private EventStore eventStore;
    

    public NotificationApplicationService() {
        super();
    }

//    @Transactional(readOnly=true)
    public NotificationLog currentNotificationLog() {
        NotificationLogFactory factory = new NotificationLogFactory(this.eventStore);

        return factory.createCurrentNotificationLog();
    }

//    @Transactional(readOnly=true)
    public NotificationLog notificationLog(String aNotificationLogId) {
        NotificationLogFactory factory = new NotificationLogFactory(this.eventStore);

        return factory.createNotificationLog(new NotificationLogId(aNotificationLogId));
    }
    
}

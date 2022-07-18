## 고쳐야 하는 것

```java
		if(availablePort(tx_id, url)) {
			url = masterUrl.get(info.getTargetUrlName()).toString();
		}else {
			url = slaveUrl.get(info.getTargetUrlName()).toString();
		}

	public static boolean availablePort(String host, String port) {
		boolean result = false;

		try {
			(new Socket(host, Integer.parseInt(port))).close();

			result = true;
		} catch (Exception e) {

		}
		return result;
	}
```

>  위 코드를 
>
> 1.  DB-Adaptor - :ok:
> 2.  DBFILE-Scheduler - :ok:
> 3.  DBFILE-Scheduler(POG) - :ok:
> 4.  DB-Provider - :ok:
> 5.  FILEDB-Scheduler..!
> 6.  REST-Provider - :ok:
> 7.  SAP-Provider - :ok:
>
> 에 알맞게 추가해야 함.  
>
> 바로 반영하지 말고 보여드릴 것.


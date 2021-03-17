@echo off
:: Server IPv6 address for your group:
set destination_IPv6=2001:610:1908:ff02:e472:904c:1d87:5b90


:: The local port on which the proxy listens for your client:
set port=1234

:: Remote IPv6 address used for connectivity testing:
set connectivity_test_IPv6=2001:610:1908:ff01:f816:3eff:fe90:98b

start TCP-Hack-Proxy.exe %destination_IPv6% %port% %connectivity_test_IPv6%

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Android\\文件管理\\practice\\胡伟龙\\项目\\家校通项目\\二期\\龚宁\\XMPP-AIDL\\xmpp_AIDL\\xmpp V0.2\\src\\jxt\\app\\stock\\xmpp\\IXmppService.aidl
 */
package jxt.app.stock.xmpp;
/**
 * Example of defining an interface for calling on to a remote service
 * (running in another process).
 */
public interface IXmppService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jxt.app.stock.xmpp.IXmppService
{
private static final java.lang.String DESCRIPTOR = "jxt.app.stock.xmpp.IXmppService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jxt.app.stock.xmpp.IXmppService interface,
 * generating a proxy if needed.
 */
public static jxt.app.stock.xmpp.IXmppService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jxt.app.stock.xmpp.IXmppService))) {
return ((jxt.app.stock.xmpp.IXmppService)iin);
}
return new jxt.app.stock.xmpp.IXmppService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_registerXmppListener:
{
data.enforceInterface(DESCRIPTOR);
jxt.app.stock.xmpp.IXmppListener _arg0;
_arg0 = jxt.app.stock.xmpp.IXmppListener.Stub.asInterface(data.readStrongBinder());
this.registerXmppListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterXmppListener:
{
data.enforceInterface(DESCRIPTOR);
jxt.app.stock.xmpp.IXmppListener _arg0;
_arg0 = jxt.app.stock.xmpp.IXmppListener.Stub.asInterface(data.readStrongBinder());
this.unregisterXmppListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sendMessage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getXmppStatus:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getXmppStatus();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getBrokerStatus:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getBrokerStatus();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_sendFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.sendFile(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_recieveFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.recieveFile(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_rejectFile:
{
data.enforceInterface(DESCRIPTOR);
this.rejectFile();
reply.writeNoException();
return true;
}
case TRANSACTION_cancelTransfer:
{
data.enforceInterface(DESCRIPTOR);
this.cancelTransfer();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jxt.app.stock.xmpp.IXmppService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
	 * 设置xmpp监听
	 * @param listener 监听对象
	 */
public void registerXmppListener(jxt.app.stock.xmpp.IXmppListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerXmppListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 取消xmpp监听
	 * @param listener 监听对象
	 */
public void unregisterXmppListener(jxt.app.stock.xmpp.IXmppListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterXmppListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 发送消息
	 * @param msg 消息
	 */
public void sendMessage(java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 获取当前Xmpp状态
	 * @return 当前xmpp状态
	 */
public boolean getXmppStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getXmppStatus, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 获取当前经纪人在线状态
	 * @return 经纪人在线状态
	 */
public java.lang.String getBrokerStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBrokerStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * 发送文件
	 * @param path 路径
	 * @param description 描述
	 */
public void sendFile(java.lang.String path, java.lang.String description) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
_data.writeString(description);
mRemote.transact(Stub.TRANSACTION_sendFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 接收文件
	 * @param path 文件存储路径
	 */
public void recieveFile(java.lang.String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_recieveFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 拒绝接收文件
	 */
public void rejectFile() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rejectFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 取消文件传输
	 */
public void cancelTransfer() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cancelTransfer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerXmppListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterXmppListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getXmppStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getBrokerStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_sendFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_recieveFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_rejectFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_cancelTransfer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
}
/**
	 * 设置xmpp监听
	 * @param listener 监听对象
	 */
public void registerXmppListener(jxt.app.stock.xmpp.IXmppListener listener) throws android.os.RemoteException;
/**
	 * 取消xmpp监听
	 * @param listener 监听对象
	 */
public void unregisterXmppListener(jxt.app.stock.xmpp.IXmppListener listener) throws android.os.RemoteException;
/**
	 * 发送消息
	 * @param msg 消息
	 */
public void sendMessage(java.lang.String message) throws android.os.RemoteException;
/**
	 * 获取当前Xmpp状态
	 * @return 当前xmpp状态
	 */
public boolean getXmppStatus() throws android.os.RemoteException;
/**
	 * 获取当前经纪人在线状态
	 * @return 经纪人在线状态
	 */
public java.lang.String getBrokerStatus() throws android.os.RemoteException;
/**
	 * 发送文件
	 * @param path 路径
	 * @param description 描述
	 */
public void sendFile(java.lang.String path, java.lang.String description) throws android.os.RemoteException;
/**
	 * 接收文件
	 * @param path 文件存储路径
	 */
public void recieveFile(java.lang.String path) throws android.os.RemoteException;
/**
	 * 拒绝接收文件
	 */
public void rejectFile() throws android.os.RemoteException;
/**
	 * 取消文件传输
	 */
public void cancelTransfer() throws android.os.RemoteException;
}

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Android\\�ļ�����\\practice\\��ΰ��\\��Ŀ\\��Уͨ��Ŀ\\����\\����\\XMPP-AIDL\\xmpp_AIDL\\xmpp V0.2\\src\\jxt\\app\\stock\\xmpp\\IXmppListener.aidl
 */
package jxt.app.stock.xmpp;
/**
 * Example of defining an interface for calling on to a remote service
 * (running in another process).
 */
public interface IXmppListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jxt.app.stock.xmpp.IXmppListener
{
private static final java.lang.String DESCRIPTOR = "jxt.app.stock.xmpp.IXmppListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jxt.app.stock.xmpp.IXmppListener interface,
 * generating a proxy if needed.
 */
public static jxt.app.stock.xmpp.IXmppListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jxt.app.stock.xmpp.IXmppListener))) {
return ((jxt.app.stock.xmpp.IXmppListener)iin);
}
return new jxt.app.stock.xmpp.IXmppListener.Stub.Proxy(obj);
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
case TRANSACTION_onNewMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
long _arg2;
_arg2 = data.readLong();
this.onNewMessage(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onXmppStatus:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.onXmppStatus(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onBrokerStatus:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onBrokerStatus(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onTransfeStatus:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onTransfeStatus(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onTransfeProgress:
{
data.enforceInterface(DESCRIPTOR);
double _arg0;
_arg0 = data.readDouble();
this.onTransfeProgress(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onFileRequest:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
long _arg3;
_arg3 = data.readLong();
this.onFileRequest(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jxt.app.stock.xmpp.IXmppListener
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
	 * �յ�����Ϣ
	 * @param broker ������
	 * @param msg ����Ϣ
	 * @param date ʱ��
	 */
public void onNewMessage(java.lang.String broker, java.lang.String msg, long date) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(broker);
_data.writeString(msg);
_data.writeLong(date);
mRemote.transact(Stub.TRANSACTION_onNewMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * Xmpp״̬��STATEö��
	 * @param Status ��ǰ״̬
	 */
public void onXmppStatus(boolean Status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((Status)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_onXmppStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * ����������״̬
	 * @param status ״̬
	 */
public void onBrokerStatus(java.lang.String status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(status);
mRemote.transact(Stub.TRANSACTION_onBrokerStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * �ļ�������״̬
	 * @param status ״̬
	 */
public void onTransfeStatus(java.lang.String status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(status);
mRemote.transact(Stub.TRANSACTION_onTransfeStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * �ļ������н���
	 * @param progress ����
	 */
public void onTransfeProgress(double progress) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeDouble(progress);
mRemote.transact(Stub.TRANSACTION_onTransfeProgress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * �ļ���������
	 * @param name �ļ���
	 * @param description �ļ�����
	 * @param type �ļ�����
	 * @param size �ļ���С
	 */
public void onFileRequest(java.lang.String name, java.lang.String description, java.lang.String type, long size) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
_data.writeString(description);
_data.writeString(type);
_data.writeLong(size);
mRemote.transact(Stub.TRANSACTION_onFileRequest, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onNewMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onXmppStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onBrokerStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onTransfeStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onTransfeProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onFileRequest = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
/**
	 * �յ�����Ϣ
	 * @param broker ������
	 * @param msg ����Ϣ
	 * @param date ʱ��
	 */
public void onNewMessage(java.lang.String broker, java.lang.String msg, long date) throws android.os.RemoteException;
/**
	 * Xmpp״̬��STATEö��
	 * @param Status ��ǰ״̬
	 */
public void onXmppStatus(boolean Status) throws android.os.RemoteException;
/**
	 * ����������״̬
	 * @param status ״̬
	 */
public void onBrokerStatus(java.lang.String status) throws android.os.RemoteException;
/**
	 * �ļ�������״̬
	 * @param status ״̬
	 */
public void onTransfeStatus(java.lang.String status) throws android.os.RemoteException;
/**
	 * �ļ������н���
	 * @param progress ����
	 */
public void onTransfeProgress(double progress) throws android.os.RemoteException;
/**
	 * �ļ���������
	 * @param name �ļ���
	 * @param description �ļ�����
	 * @param type �ļ�����
	 * @param size �ļ���С
	 */
public void onFileRequest(java.lang.String name, java.lang.String description, java.lang.String type, long size) throws android.os.RemoteException;
}

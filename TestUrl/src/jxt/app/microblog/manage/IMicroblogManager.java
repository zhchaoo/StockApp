/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\workspace\\MicroblogService\\src\\jxt\\app\\microblog\\manage\\IMicroblogManager.aidl
 */
package jxt.app.microblog.manage;
public interface IMicroblogManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements jxt.app.microblog.manage.IMicroblogManager
{
private static final java.lang.String DESCRIPTOR = "jxt.app.microblog.manage.IMicroblogManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an jxt.app.microblog.manage.IMicroblogManager interface,
 * generating a proxy if needed.
 */
public static jxt.app.microblog.manage.IMicroblogManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof jxt.app.microblog.manage.IMicroblogManager))) {
return ((jxt.app.microblog.manage.IMicroblogManager)iin);
}
return new jxt.app.microblog.manage.IMicroblogManager.Stub.Proxy(obj);
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
case TRANSACTION_AddContacts:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.AddContacts(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_DelContacts:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.DelContacts(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_GetMicroblogById:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.GetMicroblogById(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_GetMicroblog:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.GetMicroblog();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_UpdateMicroblog:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.UpdateMicroblog();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements jxt.app.microblog.manage.IMicroblogManager
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
public boolean AddContacts(java.lang.String u_id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(u_id);
mRemote.transact(Stub.TRANSACTION_AddContacts, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean DelContacts(java.lang.String u_id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(u_id);
mRemote.transact(Stub.TRANSACTION_DelContacts, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String GetMicroblogById(java.lang.String u_id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(u_id);
mRemote.transact(Stub.TRANSACTION_GetMicroblogById, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String GetMicroblog() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_GetMicroblog, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.util.List<java.lang.String> UpdateMicroblog() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_UpdateMicroblog, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_AddContacts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_DelContacts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_GetMicroblogById = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_GetMicroblog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_UpdateMicroblog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public boolean AddContacts(java.lang.String u_id) throws android.os.RemoteException;
public boolean DelContacts(java.lang.String u_id) throws android.os.RemoteException;
public java.lang.String GetMicroblogById(java.lang.String u_id) throws android.os.RemoteException;
public java.lang.String GetMicroblog() throws android.os.RemoteException;
public java.util.List<java.lang.String> UpdateMicroblog() throws android.os.RemoteException;
}

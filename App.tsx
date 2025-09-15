import React from 'react';
import {
  StyleSheet,
  Text,
  TextInput,
  Button,
  PermissionsAndroid,
  ScrollView,
  View,
  StatusBar,
} from 'react-native';

import NativeLocalStorage from './specs/NativeLocalStorage';
import NativeContacts from './specs/NativeContacts';
import NativeNotification from './specs/NativeNotification';
import { SafeAreaView } from 'react-native-safe-area-context';

const EMPTY = '<empty>';

function App(): React.JSX.Element {
  const [value, setValue] = React.useState<string | null>(null);
  const [contacts, setContacts] = React.useState<{name: string; phone: string}[] | null>(null);

  const [editingValue, setEditingValue] = React.useState<
    string | null
  >(null);

  React.useEffect(() => {
    const storedValue = NativeLocalStorage?.getItem('myKey');
    setValue(storedValue ?? '');
  }, []);

  function saveValue() {
    const dataFromM = NativeLocalStorage?.setItem(editingValue ?? EMPTY, 'myKey');
    console.log(`Data saved: ${dataFromM}`);
    
    setValue(editingValue);
  }

  function clearAll() {
    NativeLocalStorage?.clear();
    setValue('');
  }

  function deleteValue() {
    NativeLocalStorage?.removeItem('myKey');
    setValue('');
  }

  async function getContacts() {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.READ_CONTACTS
    );

    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
      const start = Date.now();
      const contactsJson = NativeContacts?.getContactList();
      
      const contactsPhone = JSON.parse(contactsJson);
      const finish = Date.now() - start;
      console.log('Finish => ', finish);
      
      setContacts(contactsPhone);
    } else {
      console.warn("Permission denied");
    }
  }

  async function showPreparing() {
    console.log(NativeNotification.showPreparing());
  }

  return (
    <SafeAreaView style={{flex: 1}}>
      <StatusBar barStyle='default' />
      <Text style={styles.text}>
        Current stored value is: {value ?? 'No Value'}
      </Text>
      <TextInput
        placeholder="Enter the text you want to store"
        style={styles.textInput}
        onChangeText={setEditingValue}
      />
      <Button title="Save" onPress={saveValue} />
      <Button title="Delete" onPress={deleteValue} />
      <Button title="Clear" onPress={clearAll} />
      <Button title="Get Contacts" onPress={getContacts} />
      <Button title="Show Preparing" onPress={showPreparing} />
      <ScrollView>
        <View>
          <Text style={styles.text}>
            Contacts:
          </Text>
          {contacts ? (
            <View>
              {contacts.map((contact, index) => (
                <Text key={index} style={styles.text}>
                  {contact.name} - {contact.phone}
                </Text>
              ))}
            </View>
          ) : (
            <Text style={styles.text}>No contacts available</Text>
          )}
          {Array.isArray(contacts) && contacts.length === 0 && <Text style={styles.text}>No contacts on your device</Text>}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  text: {
    margin: 10,
    fontSize: 20,
  },
  textInput: {
    margin: 10,
    height: 40,
    borderColor: 'black',
    borderWidth: 1,
    paddingLeft: 5,
    paddingRight: 5,
    borderRadius: 5,
  },
});

export default App;

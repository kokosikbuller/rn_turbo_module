import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  showPreparing(): string;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'NativeNotification',
);
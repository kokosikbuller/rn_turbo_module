import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  setItem(value: string, key: string): string | void;
  getItem(key: string): string | null;
  getBatteryState(): string;
  removeItem(key: string): void;
  clear(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'NativeLocalStorage',
);